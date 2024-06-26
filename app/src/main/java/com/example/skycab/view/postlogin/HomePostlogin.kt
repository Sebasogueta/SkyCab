package com.example.skycab.view.postlogin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skycab.models.Flight
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontHeader
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


@Composable
fun HomePostlogin(
    navController: NavController,
    userViewModel: UserViewModel,
) {
    val isPilotView by userViewModel.isPilotView.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "",
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterVertically)
                    .graphicsLayer(alpha = 0f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "SkyCab",
                color = text,
                fontSize = 35.sp,
                fontFamily = FontHeader
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "EditProfile",
                modifier = Modifier
                    .clickable { navController.navigate("EditProfile") }
                    .size(35.dp)
                    .align(Alignment.CenterVertically)
            )

        }
        if (isPilotView) {
            Text(
                text = "pilot",
                color = text,
                fontSize = 25.sp,
                fontFamily = FontTitle
            )
            HomePostLoginPilot(
                navController = navController,
                userViewModel = userViewModel
            )
        } else {
            Text(
                text = "user",
                color = text,
                fontSize = 25.sp,
                fontFamily = FontTitle
            )
            HomePostLoginUser(navController = navController, userViewModel = userViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePostLoginPilot(navController: NavController, userViewModel: UserViewModel) {

    var departureAirport by remember { mutableStateOf("") }
    var arrivalAirport by remember { mutableStateOf("") }
    var totalSeats by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf("") }
    var departureTime by remember { mutableStateOf("") }
    var arrivalDate by remember { mutableStateOf("") }
    var arrivalTime by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(16.dp),
        reverseLayout = false,
    ) {
        item {
            Text(
                text = "Create a New Flight",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = departureAirport,
                onValueChange = { departureAirport = it },
                label = { Text("Departure") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            TextField(
                value = arrivalAirport,
                onValueChange = { arrivalAirport = it },
                label = { Text("Destination") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            TextField(
                value = totalSeats,
                onValueChange = { if (it.all { char -> char.isDigit() }) totalSeats = it },
                label = { Text("Number of seats offered") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    readOnly = true,
                    value = departureDate,
                    onValueChange = {

                        departureDate = it

                    },
                    label = { Text("Departure date") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            showDatePicker(context) { selectedDate ->
                                departureDate = selectedDate
                            }
                        }
                )
                IconButton(onClick = {
                    showDatePicker(context) { selectedDate ->
                        departureDate = selectedDate
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select departure date"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    readOnly = true,
                    value = departureTime,
                    onValueChange = {

                        departureTime = it

                    },
                    label = { Text("Departure time") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            showTimePicker(context) { selectedTime ->
                                departureTime = selectedTime
                            }
                        }
                )
                IconButton(onClick = {
                    showTimePicker(context) { selectedTime ->
                        departureTime = selectedTime
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select departure time"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    readOnly = true,
                    value = arrivalDate,
                    onValueChange = {

                        arrivalDate = it

                    },
                    label = { Text("Arrival date") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            showDatePicker(context) { selectedDate ->
                                arrivalDate = selectedDate
                            }
                        }
                )
                IconButton(onClick = {
                    showDatePicker(context) { selectedDate ->
                        arrivalDate = selectedDate
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select arrival date"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    readOnly = true,
                    value = arrivalTime,
                    onValueChange = {
                        arrivalTime = it
                    },
                    label = { Text("Arrival time") },
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            showTimePicker(context) { selectedTime ->
                                arrivalTime = selectedTime
                            }
                        }
                )
                IconButton(onClick = {
                    showTimePicker(context) { selectedTime ->
                        arrivalTime = selectedTime
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select arrival time"
                    )
                }
            }

            TextField(
                value = price,
                onValueChange = { input ->
                    val isNumeric = input.all { char -> char.isDigit() || char == '.' }
                    val hasValidDecimalPlaces = input.split('.').let {
                        //Admite numeros sin decimales, o con 2 decimales como máximo
                        it.size == 1 || (it.size == 2 && it[1].length <= 2)
                    }

                    if (isNumeric && hasValidDecimalPlaces) {
                        price = input
                    }
                },
                label = { Text("Price") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    if (departureAirport.isNotEmpty() && arrivalAirport.isNotEmpty() && totalSeats.isNotEmpty() && departureDate.isNotEmpty() && departureTime.isNotEmpty() && arrivalDate.isNotEmpty() && arrivalTime.isNotEmpty() && price.isNotEmpty()) {
                        val departureDateTimeParsed = LocalDateTime.parse(
                            "$departureDate $departureTime",
                            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                        )
                        val arrivalDateTimeParsed = LocalDateTime.parse(
                            "$arrivalDate $arrivalTime",
                            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                        )
                        val totalSeatsParseInt = totalSeats.toInt()
                        val priceDouble = price.toDouble()
                        if (totalSeatsParseInt > 0 && priceDouble > 0) {
                            if (departureDateTimeParsed.isAfter(LocalDateTime.now()) && departureDateTimeParsed.isBefore(
                                    arrivalDateTimeParsed
                                )
                            ) {
                                userViewModel.createFlight(
                                    departureAirport,
                                    arrivalAirport,
                                    totalSeatsParseInt,
                                    departureDateTimeParsed,
                                    arrivalDateTimeParsed,
                                    priceDouble
                                ) {
                                    navController.navigate("HomePostlogin")
                                    Toast.makeText(
                                        context,
                                        "The flight has been posted.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "The dates you entered are not valid.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "You must offer at least 1 seat.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Every field is mandatory, please complete all the fields.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Post new flight")
            }
        }
    }

}

@SuppressLint("DefaultLocale")
private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val formattedDate = "${String.format("%02d", selectedDay)}-${
                String.format(
                    "%02d",
                    selectedMonth + 1
                )
            }-${selectedYear}"
            onDateSelected(formattedDate)
        }, year, month, day
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}


private fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val formattedTime =
                "${String.format("%02d", selectedHour)}:${String.format("%02d", selectedMinute)}"
            onTimeSelected(formattedTime)
        }, hour, minute, true // true para formato de 24 horas, false para formato de 12 horas
    )
    timePickerDialog.show()
}

@Composable
fun HomePostLoginUser(
    navController: NavController,
    userViewModel: UserViewModel
) {
    var flights by remember { mutableStateOf<List<Flight>>(emptyList()) }
    var flightsFiltered by remember { mutableStateOf<List<Flight>>(emptyList()) }
    val userId by remember { mutableStateOf(userViewModel.getUserId()) }


    // Fetch flights when the composable is first displayed
    LaunchedEffect(Unit) {
        userViewModel.getFlights { fetchedFlights ->
            flights = fetchedFlights
            flightsFiltered = fetchedFlights

            // No mostrar vuelos creados por el usuario actual
            flightsFiltered = flights.filter { flight ->
                flight.pilotId != userId && flight.passengers.size < flight.totalSeats
            }

        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = rememberLazyListState(),
        contentPadding = PaddingValues(16.dp),
        reverseLayout = false,
    ) {
        items(flightsFiltered.size) { index ->
            val flight = flightsFiltered[index]
            FlightCard(flight, userViewModel, userId)
        }
    }
}

@Composable
private fun FlightCard(flight1: Flight, userViewModel: UserViewModel, userId: String) {
    var flight by remember { mutableStateOf(flight1) }
    var booked by remember { mutableStateOf(flight1.passengers.contains(userId)) }
    val context = LocalContext.current

    // Convertir strings a LocalDateTime
    val departureDateTime = LocalDateTime.parse(flight.departureDateTime)
    val arrivalDateTime = LocalDateTime.parse(flight.arrivalDateTime)
    // Formato de fecha y hora
    val departureDate =
        departureDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val departureTime = departureDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
    val arrivalTime = arrivalDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

    Card(
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var pilotUser by remember { mutableStateOf("Pilot") }
                LaunchedEffect(flight.pilotId) {
                    userViewModel.getUserById(flight.pilotId) { name ->
                        pilotUser = name
                    }
                }

                Text(text = pilotUser, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = departureDate,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            val availableSeats = (flight.totalSeats - flight.passengers.size)
            Text(text = "Seats Available: ${availableSeats}/${flight.totalSeats}", fontSize = 16.sp)
            Text(text = "Departure Location: ${flight.departureAirport}", fontSize = 16.sp)
            Text(
                text = "Departure Time: $departureTime",
                fontSize = 16.sp
            )
            Text(
                text = "Destination Location: ${flight.arrivalAirport}",
                fontSize = 16.sp
            )
            Text(
                text = "Destination Time: $arrivalTime",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${flight.price}€", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Button(onClick = {
                    if (!booked) {
                        userViewModel.buyFlightSeat(flight.flightId, context) { success ->
                            if (success) {
                                userViewModel.getFlight(flight.flightId) { updatedFlight ->
                                    flight = updatedFlight
                                }
                                booked = !booked
                            } else {
                                println("fallo")
                            }
                        }
                    } else {
                        userViewModel.cancelFlightSeat(flight.flightId) { success ->
                            if (success) {
                                userViewModel.getFlight(flight.flightId) { updatedFlight ->
                                    flight = updatedFlight
                                }
                                booked = !booked
                            } else {
                                println("fallo")
                            }
                        }

                    }
                }) {
                    Text(text = if (booked) "Cancel flight" else "Buy a seat!")
                }
            }
        }
    }
}
