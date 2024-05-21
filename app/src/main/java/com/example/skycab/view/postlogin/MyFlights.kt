package com.example.skycab.view.postlogin

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skycab.models.Flight
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MyFlights(
    navController: NavHostController, userViewModel: UserViewModel
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
            Spacer(modifier = Modifier.weight(2f))
            Text(
                text = "My Flights",
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

        Spacer(modifier = Modifier.height(16.dp))

        if (isPilotView) {
            MyFlightsPilot(navController = navController, userViewModel = userViewModel)
        } else {
            MyFlightsUser(navController = navController, userViewModel = userViewModel)
        }
    }
}

@Composable
fun MyFlightsPilot(navController: NavHostController, userViewModel: UserViewModel) {
    var flightsList by remember { mutableStateOf(listOf<Flight>()) }
    var incomingFlights by remember { mutableStateOf(listOf<Flight>()) }
    var previousFlights by remember { mutableStateOf(listOf<Flight>()) }

    LaunchedEffect(Unit) {
        userViewModel.getPilotFlights { flights ->
            flightsList = flights
            incomingFlights = flights.filter { !it.ended }
                .sortedBy {
                    LocalDateTime.parse(
                        it.departureDateTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                    )
                }
            previousFlights = flights.filter { it.ended }
                .sortedByDescending {
                    LocalDateTime.parse(
                        it.departureDateTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                    )
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Incoming flights",
            color = text,
            fontSize = 30.sp,
            fontFamily = FontTitle,
            modifier = Modifier.align(Alignment.Start)
        )
        if (incomingFlights.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                items(incomingFlights) { flight ->
                    FlightItem(flight, navController, userViewModel)
                }
            }
        } else {
            Text(
                text = "You have no incoming flights.",
                color = text,
                fontFamily = FontText,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Previous flights",
            color = text,
            fontSize = 30.sp,
            fontFamily = FontTitle,
            modifier = Modifier.align(Alignment.Start)
        )

        if (previousFlights.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                items(previousFlights) { flight ->
                    FlightItem(flight, navController, userViewModel)
                }
            }
        }else {
            Text(
                text = "You have no previous flights.",
                color = text,
                fontFamily = FontText,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun MyFlightsUser(navController: NavHostController, userViewModel: UserViewModel) {
    var flightsList by remember { mutableStateOf(listOf<Flight>()) }
    var incomingFlights by remember { mutableStateOf(listOf<Flight>()) }
    var previousFlights by remember { mutableStateOf(listOf<Flight>()) }

    LaunchedEffect(Unit) {
        userViewModel.getUserFlights { flights ->
            flightsList = flights
            incomingFlights = flights.filter { !it.ended }
                .sortedBy {
                    LocalDateTime.parse(
                        it.departureDateTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                    )
                }
            previousFlights = flights.filter { it.ended }
                .sortedByDescending {
                    LocalDateTime.parse(
                        it.departureDateTime,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                    )
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Incoming flights",
            color = text,
            fontSize = 30.sp,
            fontFamily = FontTitle,
            modifier = Modifier.align(Alignment.Start)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp)
        ) {
            if (incomingFlights.isNotEmpty()) {
                items(incomingFlights) { flight ->
                    FlightItem(flight, navController, userViewModel)
                }
            } else {
                item {
                    Text(
                        text = "You have no incoming flights.",
                        color = text,
                        fontFamily = FontText,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Previous flights",
            color = text,
            fontSize = 30.sp,
            fontFamily = FontTitle,
            modifier = Modifier.align(Alignment.Start)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp)
        ) {
            if (previousFlights.isNotEmpty()) {
                items(previousFlights) { flight ->
                    FlightItem(flight, navController, userViewModel)
                }
            } else {
                item {
                    Text(
                        text = "You have no previous flights.",
                        color = text,
                        fontFamily = FontText,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun FlightItem(flight1: Flight, navController: NavHostController, userViewModel: UserViewModel) {
    var flight by remember { mutableStateOf(flight1) }

    // Convertir strings a LocalDateTime
    val departureDateTime = LocalDateTime.parse(flight.departureDateTime)
    val arrivalDateTime = LocalDateTime.parse(flight.arrivalDateTime)

    // Formato de fecha y hora
    val departureDate =
        departureDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val departureTime = departureDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
    val arrivalTime = arrivalDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(3.dp, primary)
            .background(tertiary)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.clickable(onClick = {
                //navController.navigate("FlightDetails/${flight.flightId}")
            })
        ) {
            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "From: ${flight.departureAirport} To: ${flight.arrivalAirport}",
                    color = background,
                    fontSize = 19.sp
                )
                Text(
                    text = departureDate,
                    color = background,
                    fontSize = 19.sp
                )
                Text(
                    text = "$departureTime - $arrivalTime",
                    color = background,
                    fontSize = 19.sp
                )
            }
            IconButton(
                onClick = {
                    userViewModel.cancelAFlight(flight.flightId) { success ->
                        if (success) {
                            userViewModel.getFlight(flight.flightId) { updatedFlight ->
                                flight = updatedFlight
                            }
                        } else {
                            println("fallo")
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove flight",
                    tint = Color.Red,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

