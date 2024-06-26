package com.example.skycab.models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class UserViewModel : ViewModel() {

    val auth: FirebaseAuth = Firebase.auth
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    private val _isPilot = MutableStateFlow(false)
    private val _isPilotView = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn
    val isPilot: StateFlow<Boolean> get() = _isPilot
    val isPilotView: StateFlow<Boolean> get() = _isPilotView

    init {
        // AuthStateListener para actualizar el StateFlow cuando cambie el estado de auth
        auth.addAuthStateListener { firebaseAuth ->
            _isLoggedIn.value = firebaseAuth.currentUser != null
            // Actualiza el estado del piloto cuando el usuario cambia
            if (_isLoggedIn.value) {
                checkIfPilot()
            }
            _isPilotView.value = false
        }

    }

    fun signWithEmailAndPassword(
        email: String,
        password: String,
        context: Context,
        result: (Boolean) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result(true)
                Toast.makeText(
                    context,
                    "You have successfully logged in!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                result(false)
                Toast.makeText(
                    context,
                    "Error: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    fun registerWithEmailAndPassword(
        username: String,
        email: String,
        password: String,
        context: Context,
        result: (Boolean) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        // Verificar si el usuario ya existe en la base de datos
        checkUserExistence(username) { userExists ->
            if (!userExists) { // if username is not used
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val currentUser = auth.currentUser
                        val newUser = User(
                            username,
                            "",
                            "I am new here!",
                            mutableListOf(),
                            mutableListOf()
                        )

                        // Verificar si el usuario ya tiene un documento en Firestore
                        currentUser?.uid?.let { userId ->
                            val userDocument = usersRef.document(userId)
                            userDocument.get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    // El documento ya existe, no se puede crear de nuevo
                                    result(false)
                                } else {
                                    // El documento no existe, se crea y se guardan los datos
                                    userDocument.set(newUser)
                                        .addOnSuccessListener {
                                            Log.d(
                                                "Firestore",
                                                "Document $userId created with user data: $newUser"
                                            )
                                            result(true)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(
                                                "Firestore",
                                                "Error creating document $userId: $e"
                                            )
                                            result(false)
                                        }
                                }
                            }.addOnFailureListener { e ->
                                Log.e("Firestore", "Error checking document existence: $e")
                                result(false)
                            }
                        } ?: run {
                            Log.e("Auth", "Current user is null")
                            result(false)
                        }
                    } else {
                        result(false)
                        Toast.makeText(
                            context,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            } else {
                result(false) //user already exists
                Toast.makeText(
                    context,
                    "Error: username already exists",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkUserExistence(username: String, result: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        var result2 = false

        db.collection("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val usernameList = ArrayList<String>()
                for (document in task.result) {
                    val userData = document.toObject(User::class.java)
                    userData?.let { user ->
                        if (username.lowercase().equals(user.username.lowercase())) {
                            result2 = true //user already exists
                        }
                    }
                }
                result(result2)
            } else {
                result(true) //error
            }
        }

    }

    fun editProfile(newBio: String, newPilotLicense: String, result: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val usersRef = db.collection("users")
            val userDocumentRef = usersRef.document(currentUser.uid)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val updatedBio = hashMapOf<String, Any>()
                        updatedBio["bio"] = newBio
                        updatedBio["pilotLicense"] = newPilotLicense

                        userDocumentRef.update(updatedBio)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    _isPilot.value = newPilotLicense.isNotEmpty()
                                    if (!_isPilot.value) _isPilotView.value = false
                                    Log.d("Firestore", "Profile updated successfully")
                                    result(true)
                                } else {
                                    Log.e("Firestore", "Error updating profile: ${task.exception}")
                                    result(false)
                                }
                            }
                    }
                }
        }

    }
    fun getUserId(): String {
        val currentUser = auth.currentUser
        return currentUser!!.uid
    }
    fun getUser(callback: (String) -> Unit) {

        val currentUser = this.auth.currentUser
        currentUser?.let { firebaseUser ->
            val userId = firebaseUser.uid
            val db = FirebaseFirestore.getInstance()
            val usersRef = db.collection("users")
            val userDocument = usersRef.document(userId)
            var username = "User"


            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userData = documentSnapshot.toObject(User::class.java)
                    userData?.let { user ->
                        username = user.username
                        callback(username)

                    }
                } else {
                    username = "User"
                    callback(username)
                }
                callback.invoke(username)
            }.addOnFailureListener { e ->
                // Manejo de errores al obtener el documento del usuario
            }
        }

    }

    fun getUserById(userId:String, callback: (String) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val userDocument = usersRef.document(userId)
        var username = "User"


        userDocument.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userData = documentSnapshot.toObject(User::class.java)
                userData?.let { user ->
                    username = user.username
                    callback(username)
                }
            } else {
                username = "User"
                callback(username)
            }
            callback.invoke(username)
        }.addOnFailureListener { e ->
        // Manejo de errores al obtener el documento del usuario
            callback("Unknown user")
        }


    }

    fun getUserBio(callback: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val usersRef = db.collection("users")
            val userDocumentRef = usersRef.document(currentUser.uid)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userBio = documentSnapshot.data!!["bio"] as String
                        callback(userBio)
                    } else {
                        callback("")
                    }
                }
                .addOnFailureListener {
                    callback("")
                }
        } else {
            callback("")
        }
    }

    fun getUserLicense(callback: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val usersRef = db.collection("users")
            val userDocumentRef = usersRef.document(currentUser.uid)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val license = documentSnapshot.data!!["pilotLicense"] as String
                        callback(license)
                    } else {
                        callback("")
                    }
                }
                .addOnFailureListener {
                    callback("")
                }
        } else {
            callback("")
        }
    }

    private fun checkIfPilot() {
        getUserLicense { userLicense ->
            _isPilot.value = userLicense.isNotEmpty()
        }
    }

    fun changeView() {
        _isPilotView.value = !_isPilotView.value
    }

    fun createFlight(
        departureAirport: String,
        arrivalAirport: String,
        offeredSeats: Int,
        departureDateTime: LocalDateTime,
        arrivalDateTime: LocalDateTime,
        price: Double,
        function: () -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val flightsRef = db.collection("flights")
            val pilotId = currentUser.uid

            // Crear un mapa con los datos del vuelo
            val flightData = hashMapOf(
                "pilotId" to pilotId,
                "passengers" to mutableListOf<String>(),
                "totalSeats" to offeredSeats,
                "departureDateTime" to departureDateTime.toString(),
                "arrivalDateTime" to arrivalDateTime.toString(),
                "departureAirport" to departureAirport,
                "arrivalAirport" to arrivalAirport,
                "publicationDate" to Date().toString(),
                "price" to price
            )

            // Añadir el documento a la colección "flights"
            flightsRef.add(flightData)
                .addOnSuccessListener { documentReference ->
                    Log.d(
                        "Firestore",
                        "Flight created successfully with ID: ${documentReference.id}"
                    )
                    documentReference.update("flightId", documentReference.id)
                    //guardar id del vuelo en el user (piloto)
                    val usersRef = db.collection("users")
                    val pilotDocumentRef = usersRef.document(pilotId)
                    pilotDocumentRef.update("pilotFlightIds", FieldValue.arrayUnion(documentReference.id))
                    function()
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error creating flight", e)
                }
        } else {
            Log.e("Firestore", "No user is currently signed in")
        }
    }

    fun cancelFlight(flightId: String,context: Context, result: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val usersRef = db.collection("users")
            val pilotDocumentRef = usersRef.document(currentUser.uid)
            val flightsRef = db.collection("flights")
            val flightDocumentRef = flightsRef.document(flightId)

            flightDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val passengers1 = documentSnapshot.data!!["passengers"] as List<String>
                        //Eliminar el flightid de la lista de vuelos de cada usuario
                        for (passenger in passengers1) {
                            usersRef.document(passenger).update("flightIds", FieldValue.arrayRemove(flightId))
                        }
                        pilotDocumentRef.update("pilotFlightIds", FieldValue.arrayRemove(flightId))
                        //Eliminar documento del vuelo
                        flightDocumentRef.delete()
                            .addOnSuccessListener {
                                result(true)
                                Toast.makeText(
                                    context,
                                    "Flight canceled successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                result(false)
                                Toast.makeText(
                                    context,
                                    "Something went wrong when the flight was cancelled",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
        }
        result(false)
    }

    fun getFlights(callback: (List<Flight>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val flightsRef = db.collection("flights")

        flightsRef.get()
            .addOnSuccessListener { querySnapshot ->
                val flightsList = mutableListOf<Flight>()
                for (document in querySnapshot.documents) {
                    document.toObject(Flight::class.java)?.let { flight ->
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                        val departureDateTime = LocalDateTime.parse(flight.departureDateTime, formatter)
                        if (departureDateTime.isAfter(LocalDateTime.now())) {
                            flightsList.add(flight)
                        }
                    }
                }
                callback(flightsList)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting flights: ", exception)
                callback(emptyList())
            }
    }
    fun getUserFlights(callback: (List<Flight>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser!!.uid

        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userFlightIds = documentSnapshot.get("flightIds") as? List<String> ?: emptyList<String>()
                    if (userFlightIds.isEmpty()) {
                        callback(emptyList())
                        return@addOnSuccessListener
                    }
                    db.collection("flights")
                        .whereIn(FieldPath.documentId(), userFlightIds)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val flightsList = mutableListOf<Flight>()
                            for (document in querySnapshot.documents) {
                                document.toObject(Flight::class.java)?.let { flight ->
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                                    val arrivalDateTime = LocalDateTime.parse(flight.arrivalDateTime, formatter)
                                    if (arrivalDateTime.isBefore(LocalDateTime.now())) {
                                        flight.ended = true
                                    } else {
                                        flight.ended = false
                                    }
                                    flightsList.add(flight)
                                }
                            }
                            callback(flightsList)
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error getting flights: ", exception)
                            callback(emptyList())
                        }
                } else {
                    callback(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting user document: ", exception)
                callback(emptyList())
            }
    }

    fun getPilotFlights(callback: (List<Flight>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser!!.uid

        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val pilotFlightIds = documentSnapshot.get("pilotFlightIds") as? List<String> ?: emptyList<String>()
                    if (pilotFlightIds.isEmpty()) {
                        callback(emptyList())
                        return@addOnSuccessListener
                    }
                    db.collection("flights")
                        .whereIn(FieldPath.documentId(), pilotFlightIds)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val flightsList = mutableListOf<Flight>()
                            for (document in querySnapshot.documents) {
                                document.toObject(Flight::class.java)?.let { flight ->
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                                    val arrivalDateTime = LocalDateTime.parse(flight.arrivalDateTime, formatter)
                                    if (arrivalDateTime.isBefore(LocalDateTime.now())) {
                                        flight.ended = true
                                    } else {
                                        flight.ended = false
                                    }
                                    flightsList.add(flight)
                                }
                            }
                            callback(flightsList)
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error getting pilot flights: ", exception)
                            callback(emptyList())
                        }
                } else {
                    callback(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting pilot user document: ", exception)
                callback(emptyList())
            }
    }

    fun buyFlightSeat(flightId: String, context: Context, result: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val usersRef = db.collection("users")
            val userDocumentRef = usersRef.document(currentUser.uid)
            val flightsRef = db.collection("flights")
            val flightDocumentRef = flightsRef.document(flightId)

            flightDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val passengers1 = documentSnapshot.data!!["passengers"] as List<String>
                        val totalSeats = documentSnapshot.data!!["totalSeats"] as Long

                        // Verificar si hay asientos disponibles
                        if (totalSeats - passengers1.size > 0) {
                            val alreadyRegistered = passengers1.contains(currentUser.uid)
                            val userIsThePilot = currentUser.uid == documentSnapshot.data!!["pilotId"] as String
                            if (alreadyRegistered || userIsThePilot) {
                                Toast.makeText(context,
                                    "You are already in this flight!",
                                    Toast.LENGTH_SHORT
                                )
                                result(false)
                            } else {
                                // Agregar al usuario actual a la lista de pasajeros
                                val newPassengersList = passengers1.toMutableList()
                                newPassengersList.add(currentUser.uid)

                                // Actualizar el documento en Firestore con la nueva lista de pasajeros
                                flightDocumentRef.update("passengers", newPassengersList)
                                    .addOnSuccessListener {
                                        userDocumentRef.get()
                                            .addOnSuccessListener { documentSnapshot ->
                                                if (documentSnapshot.exists()) {
                                                    val flightIds =
                                                        documentSnapshot.data!!["flightIds"] as List<String>

                                                    val newFlightList = flightIds.toMutableList()
                                                    newFlightList.add(flightId)

                                                    userDocumentRef.update(
                                                        "flightIds",
                                                        newFlightList
                                                    )
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                Toast.makeText(context,
                                                                    "Your seat was successfully purchased, enjoy your trip!",
                                                                    Toast.LENGTH_LONG
                                                                )
                                                                Log.d(
                                                                    "Firestore",
                                                                    "FlightIDs list updated"
                                                                )
                                                                result(true)
                                                            } else {
                                                                Log.e(
                                                                    "Firestore",
                                                                    "Error updating FlightIDs: ${task.exception}"
                                                                )
                                                                result(false)
                                                            }
                                                        }
                                                } else {
                                                    result(false)
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                result(false)
                                            }
                                    }
                            }
                        }

                    } else {
                        result(false)
                    }

                }
                .addOnFailureListener { e ->
                    result(false)
                }
        }
    }

    fun cancelFlightSeat(flightId: String, result: (Boolean) -> Unit) {

        val db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val usersRef = db.collection("users")
            val userDocumentRef = usersRef.document(currentUser.uid)
            val flightsRef = db.collection("flights")
            val flightDocumentRef = flightsRef.document(flightId)

            flightDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val passengers1 = documentSnapshot.data!!["passengers"] as List<String>

                            val isRegistered = passengers1.contains(currentUser.uid)
                            if (isRegistered) {
                                // Agregar al usuario actual a la lista de pasajeros
                                val newPassengersList = passengers1.toMutableList()
                                newPassengersList.removeIf { it == currentUser.uid }

                                // Actualizar el documento en Firestore con la nueva lista de pasajeros
                                flightDocumentRef.update("passengers", newPassengersList)
                                    .addOnSuccessListener {
                                        // Actualización exitosa
                                        userDocumentRef.get()
                                            .addOnSuccessListener { documentSnapshot ->
                                                if (documentSnapshot.exists()) {
                                                    val flightIds =
                                                        documentSnapshot.data!!["flightIds"] as List<String>

                                                    val newFlightList = flightIds.toMutableList()
                                                    newFlightList.removeIf { it == flightId }

                                                    userDocumentRef.update(
                                                        "flightIds",
                                                        newFlightList
                                                    )
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                Log.d(
                                                                    "Firestore",
                                                                    "FlightIDs list updated"
                                                                )
                                                                result(true)
                                                            } else {
                                                                Log.e(
                                                                    "Firestore",
                                                                    "Error updating FlightIDs: ${task.exception}"
                                                                )
                                                                result(false)
                                                            }
                                                        }
                                                } else {
                                                    result(false)
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                result(false)
                                            }
                                    }
                            }


                    } else {
                        result(false)
                    }

                }
                .addOnFailureListener { e ->
                    result(false)
                }
        }
    }
    fun getFlight(flightId: String, callback: (Flight) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val flightRef = db.collection("flights").document(flightId)

        flightRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val flight = documentSnapshot.toObject(Flight::class.java)
                flight?.let { callback(it) }
            }
        }.addOnFailureListener { e ->
            Log.e("Firestore", "Error getting flight", e)
        }
    }

}




