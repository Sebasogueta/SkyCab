package com.example.skycab.view.postlogin

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skycab.models.Flight
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontHeader
import com.example.skycab.ui.theme.text
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    var departureCity by remember { mutableStateOf("") }
    var destinationCity by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val context = LocalContext.current
    var flights by remember { mutableStateOf<List<Flight>>(emptyList()) }
    var flightsFiltered by remember { mutableStateOf<List<Flight>>(emptyList()) }
    val userId by remember { mutableStateOf(userViewModel.getUserId()) }
    var isFirstTime by remember { mutableStateOf(true) }

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
            Spacer(modifier = Modifier.weight(2f))
            Text(
                text = "Search a flight",
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp),
                color = text,
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

        TextField(
            value = departureCity,
            onValueChange = { departureCity = it },
            label = { Text("Departure City") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        TextField(
            value = destinationCity,
            onValueChange = { destinationCity = it },
            label = { Text("Destination City") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                readOnly = true,
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (DD-MM-YYYY)") },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker(context) { selectedDate -> date = selectedDate } }
            )
            IconButton(onClick = { showDatePicker(context) { selectedDate -> date = selectedDate } }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }

        val date1Format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date2Format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val departureDateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())

        Button(
            onClick = {
                isFirstTime = false
                userViewModel.getFlights { fetchedFlights ->
                    flights = fetchedFlights
                    flightsFiltered = fetchedFlights

                    val formattedDate2 = if (date.isNotEmpty()) {
                        val formattedDate1 = date1Format.parse(date)
                        date2Format.format(formattedDate1)
                    } else ""

                    // Update the flight list based on search criteria
                    flightsFiltered = flights.filter { flight ->
                        val departureDate = departureDateTimeFormat.parse(flight.departureDateTime)
                        val formattedDepartureDate = date2Format.format(departureDate)

                        val matchesDepartureCity = departureCity.isEmpty() || flight.departureAirport.equals(departureCity, ignoreCase = true)
                        val matchesDestinationCity = destinationCity.isEmpty() || flight.arrivalAirport.equals(destinationCity, ignoreCase = true)
                        val matchesDate = date.isEmpty() || formattedDepartureDate == formattedDate2

                        matchesDepartureCity && matchesDestinationCity && matchesDate
                    }
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Search")
        }

        if (flightsFiltered.isEmpty() && !isFirstTime) {
            Text(
                text = "No flights match your criteria.",
                fontSize = 16.sp,
                color = Color.Red,
                modifier = Modifier.padding(20.dp)
            )
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(flightsFiltered) { flight ->
                    FlightCard(flight, userViewModel, userId)
                }
            }
        }
    }
}

private fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val formattedDate = "${String.format("%02d", selectedDay)}-${String.format("%02d", selectedMonth + 1)}-${selectedYear}"
            onDateSelected(formattedDate)
        }, year, month, day
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    datePickerDialog.show()
}

@Composable
private fun FlightCard(flight1: Flight, userViewModel: UserViewModel, userId: String) {
    var flight by remember { mutableStateOf(flight1) }
    var booked by remember { mutableStateOf(flight1.passengers.contains(userId)) }

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
                        userViewModel.buyFlightSeat(flight.flightId) { success ->
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
                        userViewModel.cancelAFlight(flight.flightId) { success ->
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
