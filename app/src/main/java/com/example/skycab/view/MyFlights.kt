package com.example.skycab.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skycab.models.Flight
import com.example.skycab.models.User
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.*
import com.google.type.DateTime
import java.sql.Date
import java.time.LocalDateTime

@Composable
fun MyFlights(
    navController: NavHostController, userViewModel: UserViewModel
) {
    // Dummy data TODO ELIMINAR DATOS DE PRUEBA
    val dummyUser = User("JohnDoe","","I am new Here!")
    val dummyIncomingFlights = remember {
        mutableListOf(
            Flight(
                flightId = 1,
                pilotUsername = "PilotA",
                passengers = mutableListOf(dummyUser),
                totalSeats = 4,
                departureDateTime = LocalDateTime.of(2024, 6, 1, 14, 0),
                arrivalDateTime = LocalDateTime.of(2024, 6, 1, 16, 0),
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                publicationDate = Date(2024, 5, 1),
                ended = false
            ),
            Flight(
                flightId = 2,
                pilotUsername = "PilotB",
                passengers = mutableListOf(dummyUser),
                totalSeats = 6,
                departureDateTime = LocalDateTime.of(2024, 6, 2, 10, 0),
                arrivalDateTime = LocalDateTime.of(2024, 6, 2, 12, 0),
                departureAirport = "ORD",
                arrivalAirport = "ATL",
                publicationDate = Date(2024, 5, 2),
                ended = false
            ),
            Flight(
                flightId = 3,
                pilotUsername = "PilotB",
                passengers = mutableListOf(dummyUser),
                totalSeats = 6,
                departureDateTime = LocalDateTime.of(2024, 6, 2, 10, 0),
                arrivalDateTime = LocalDateTime.of(2024, 6, 2, 12, 0),
                departureAirport = "ORD",
                arrivalAirport = "ATL",
                publicationDate = Date(2024, 5, 2),
                ended = false
            ),
            Flight(
                flightId = 4,
                pilotUsername = "PilotB",
                passengers = mutableListOf(dummyUser),
                totalSeats = 6,
                departureDateTime = LocalDateTime.of(2024, 6, 2, 10, 0),
                arrivalDateTime = LocalDateTime.of(2024, 6, 2, 12, 0),
                departureAirport = "ORD",
                arrivalAirport = "ATL",
                publicationDate = Date(2024, 5, 2),
                ended = false
            )

        )
    }
    val dummyPreviousFlights = remember {
        mutableListOf(
            Flight(
                flightId = 5,
                pilotUsername = "PilotC",
                passengers = mutableListOf(dummyUser),
                totalSeats = 4,
                departureDateTime = LocalDateTime.of(2024, 5, 1, 14, 0),
                arrivalDateTime = LocalDateTime.of(2024, 5, 1, 16, 0),
                departureAirport = "MIA",
                arrivalAirport = "BOS",
                publicationDate = Date(2024, 4, 1),
                ended = true
            ),
            Flight(
                flightId = 6,
                pilotUsername = "PilotD",
                passengers = mutableListOf(dummyUser),
                totalSeats = 6,
                departureDateTime = LocalDateTime.of(2024, 4, 15, 10, 0),
                arrivalDateTime = LocalDateTime.of(2024, 4, 15, 12, 0),
                departureAirport = "SFO",
                arrivalAirport = "SEA",
                publicationDate = Date(2024, 3, 15),
                ended = true
            ),
            Flight(
                flightId = 7,
                pilotUsername = "PilotD",
                passengers = mutableListOf(dummyUser),
                totalSeats = 6,
                departureDateTime = LocalDateTime.of(2024, 4, 15, 10, 0),
                arrivalDateTime = LocalDateTime.of(2024, 4, 15, 12, 0),
                departureAirport = "SFO",
                arrivalAirport = "SEA",
                publicationDate = Date(2024, 3, 15),
                ended = true
            ),
            Flight(
                flightId = 8,
                pilotUsername = "PilotD",
                passengers = mutableListOf(dummyUser),
                totalSeats = 6,
                departureDateTime = LocalDateTime.of(2024, 4, 15, 10, 0),
                arrivalDateTime = LocalDateTime.of(2024, 4, 15, 12, 0),
                departureAirport = "SFO",
                arrivalAirport = "SEA",
                publicationDate = Date(2024, 3, 15),
                ended = true
            )
        )
    }

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
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "My Flights",
                color = text,
                fontSize = 35.sp,
                fontFamily = FontTitle
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

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
            if (dummyIncomingFlights.isNotEmpty()) {
                items(dummyIncomingFlights) { flight ->
                    FlightItem(flight, navController)
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
            if (dummyPreviousFlights.isNotEmpty()) {
                items(dummyPreviousFlights) { flight ->
                    FlightItem(flight, navController)
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
fun FlightItem(flight: Flight, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(3.dp, primary)
            .background(tertiary)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.clickable(onClick = {
                navController.navigate("FlightDetails")
            })
        ) {
            Text(
                text = "${flight.departureAirport} -> ${flight.arrivalAirport}\n${flight.departureDateTime} - ${flight.arrivalDateTime}",
                modifier = Modifier
                    .weight(4f)
                    .padding(8.dp),
                color = background,
                fontSize = 19.sp
            )
            IconButton(
                onClick = {
                    // Handle favorite action
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}



