package com.example.skycab.view.postlogin

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import java.sql.Date
import java.time.LocalDateTime

// Dummy data TODO ELIMINAR DATOS DE PRUEBA
val dummyUser = User("JohnDoe","","I am new Here!", mutableListOf())
val dummyUserString = "John Doe"
val dummyIncomingFlights = {
    mutableListOf(
        Flight(
            flightId = "1",
            pilotId = "PilotA",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 4,
            departureDateTime = LocalDateTime.of(2024, 6, 1, 14, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 6, 1, 16, 0).toString(),
            departureAirport = "JFK",
            arrivalAirport = "LAX",
            publicationDate = Date(2024, 5, 1).toString(),
            ended = false, price = 200.0
        ),
        Flight(
            flightId = "2",
            pilotId = "PilotB",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 6,
            departureDateTime = LocalDateTime.of(2024, 6, 2, 10, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 6, 2, 12, 0).toString(),
            departureAirport = "ORD",
            arrivalAirport = "ATL",
            publicationDate = Date(2024, 5, 2).toString(),
            ended = false, price = 200.0
        ),
        Flight(
            flightId = "3",
            pilotId = "PilotB",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 6,
            departureDateTime = LocalDateTime.of(2024, 6, 2, 10, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 6, 2, 12, 0).toString(),
            departureAirport = "ORD",
            arrivalAirport = "ATL",
            publicationDate = Date(2024, 5, 2).toString(),
            ended = false, price = 200.0
        ),
        Flight(
            flightId = "4",
            pilotId = "PilotB",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 6,
            departureDateTime = LocalDateTime.of(2024, 6, 2, 10, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 6, 2, 12, 0).toString(),
            departureAirport = "ORD",
            arrivalAirport = "ATL",
            publicationDate = Date(2024, 5, 2).toString(),
            ended = false, price = 200.0
        )

    )
}
val dummyPreviousFlights = {
    mutableListOf(
        Flight(
            flightId = "5",
            pilotId = "PilotC",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 4,
            departureDateTime = LocalDateTime.of(2024, 5, 1, 14, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 5, 1, 16, 0).toString(),
            departureAirport = "MIA",
            arrivalAirport = "BOS",
            publicationDate = Date(2024, 4, 1).toString(),
            ended = true, price = 200.0
        ),
        Flight(
            flightId = "6",
            pilotId = "PilotD",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 6,
            departureDateTime = LocalDateTime.of(2024, 4, 15, 10, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 4, 15, 12, 0).toString(),
            departureAirport = "SFO",
            arrivalAirport = "SEA",
            publicationDate = Date(2024, 3, 15).toString(),
            ended = true, price = 200.0
        ),
        Flight(
            flightId = "7",
            pilotId = "PilotD",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 6,
            departureDateTime = LocalDateTime.of(2024, 4, 15, 10, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 4, 15, 12, 0).toString(),
            departureAirport = "SFO",
            arrivalAirport = "SEA",
            publicationDate = Date(2024, 3, 15).toString(),
            ended = true, price = 200.0
        ),
        Flight(
            flightId = "8",
            pilotId = "PilotD",
            passengers = mutableListOf(dummyUserString),
            totalSeats = 6,
            departureDateTime = LocalDateTime.of(2024, 4, 15, 10, 0).toString(),
            arrivalDateTime = LocalDateTime.of(2024, 4, 15, 12, 0).toString(),
            departureAirport = "SFO",
            arrivalAirport = "SEA",
            publicationDate = Date(2024, 3, 15).toString(),
            ended = true, price = 200.0
        )
    )
}

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

        if (isPilotView) {
            MyFlightsPilot(navController = navController, userViewModel = userViewModel)
        } else {
            MyFlightsUser(navController = navController, userViewModel = userViewModel)
        }
    }
}

@Composable
fun MyFlightsPilot(navController: NavHostController, userViewModel: UserViewModel){
    Text(text = "Pilot view")
}
@Composable
fun MyFlightsUser(navController: NavHostController, userViewModel: UserViewModel){

    val dummyIncomingFlights: MutableList<Flight> = dummyIncomingFlights.invoke() /* TODO BORRAR DATOS DE PUREBA */
    val dummyPreviousFlights: MutableList<Flight> = dummyPreviousFlights.invoke() /* TODO BORRAR DATOS DE PUREBA */

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



