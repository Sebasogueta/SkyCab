package com.example.skycab.models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
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
                            mutableListOf()
                        ) /* TODO AGREGAR MYFLIGHTS */

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
            val pilotEmail = currentUser.email

            // Crear un mapa con los datos del vuelo
            val flightData = hashMapOf(
                "pilotEmail" to pilotEmail,
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
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error creating flight", e)
                }
        } else {
            Log.e("Firestore", "No user is currently signed in")
        }
    }

    fun getFlights(callback: (List<Flight>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val flightsRef = db.collection("flights")

        flightsRef.get()
            .addOnSuccessListener { querySnapshot ->
                val flightsList = mutableListOf<Flight>()
                for (document in querySnapshot.documents) {
                    document.toObject(Flight::class.java)?.let { flight ->
                        flightsList.add(flight)
                    }
                }
                callback(flightsList)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting flights: ", exception)
                callback(emptyList())
            }
    }

    fun bookAFlight(flightId: String, result: (Boolean) -> Unit) {

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
                            if (alreadyRegistered) {
                                // TODO: Mostrar Toast - Ya registrado
                                result(false)
                            } else {
                                // Agregar al usuario actual a la lista de pasajeros
                                val newPassengersList = passengers1.toMutableList()
                                newPassengersList.add(currentUser.uid)

                                // Actualizar el documento en Firestore con la nueva lista de pasajeros
                                flightDocumentRef.update("passengers", newPassengersList)
                                    .addOnSuccessListener {
                                        // TODO: Mostrar Toast - Registro exitoso
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
                                                // TODO: Manejar el fallo de la actualización
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
                    // TODO: Manejar el fallo
                    result(false)
                }
        }
    }
}

/*
fun getFavoriteCities(callback: (MutableList<String>) -> Unit) {
    val currentUser = this.auth.currentUser
    currentUser?.let { firebaseUser ->
        val userId = firebaseUser.uid
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val userDocument = usersRef.document(userId)
        var favCities = mutableListOf<String>()


        userDocument.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val userData = documentSnapshot.toObject(User::class.java)
                userData?.let { user ->
                    favCities = user.favorites
                    callback(favCities)
                    // Actualización del estado de favoriteCities cuando se completa la operación
                }
            } else {
                // El documento del usuario no existe
            }
            callback.invoke(favCities)
        }.addOnFailureListener { e ->
            // Manejo de errores al obtener el documento del usuario
        }
    }
}

fun removeFavorite(favoriteCities: MutableList<String>, favoriteCity: String, callback: (MutableList<String>) -> Unit){

val db = FirebaseFirestore.getInstance()
val currentUser = auth.currentUser

if (currentUser != null) {
    val usersRef = db.collection("users")
    val userDocumentRef = usersRef.document(currentUser.uid)

    userDocumentRef.get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val favoritesList = documentSnapshot.data!!["favorites"] as MutableList<String>
                favoritesList.remove(favoriteCity)

                val updatedMap = hashMapOf<String, Any>()
                updatedMap["favorites"] = favoritesList

                userDocumentRef.update(updatedMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Firestore", "Favorites updated successfully")
                            callback(favoritesList)
                        } else {
                            Log.e("Firestore", "Error updating favorites: ${task.exception}")
                            callback(favoritesList)
                        }
                    }
            }
        }
}
}

fun addFavorite(favoriteCities: MutableList<String>, favoriteCity: String, callback: (MutableList<String>) -> Unit){

    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val usersRef = db.collection("users")
        val userDocumentRef = usersRef.document(currentUser.uid)

        userDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val favoritesList = documentSnapshot.data!!["favorites"] as MutableList<String>
                    favoritesList.add(favoriteCity)

                    val updatedMap = hashMapOf<String, Any>()
                    updatedMap["favorites"] = favoritesList

                    userDocumentRef.update(updatedMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("Firestore", "Favorites updated successfully")
                                callback(favoritesList)
                            } else {
                                Log.e("Firestore", "Error updating favorites: ${task.exception}")
                                callback(favoritesList)
                            }
                        }
                }
            }
    }
}

 */



