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
import com.example.skycab.models.UserViewModel
import com.example.skycab.ui.theme.FontTitle
import com.example.skycab.ui.theme.text
import java.util.Calendar

data class FlightInfo(
    val name: String,
    val date: String,
    val seatsAvailable: String,
    val departureLocation: String,
    val departureTime: String,
    val destinationLocation: String,
    val destinationTime: String,
    val price: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    var departureCity by remember { mutableStateOf("") }
    var destinationCity by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val flights = remember { mutableStateOf(sampleFlights()) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Search a flight",
            fontSize = 30.sp,
            modifier = Modifier.padding(16.dp),
            color = text,
            fontFamily = FontTitle
        )

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

        Button(
            onClick = {
                // Update the flight list based on search criteria
                flights.value = sampleFlights().filter {
                    it.departureLocation.contains(departureCity, ignoreCase = true) &&
                            it.destinationLocation.contains(destinationCity, ignoreCase = true) &&
                            it.date == date // Ensure the date format matches
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Search")
        }
        if (flights.value.isEmpty()) {
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
                items(flights.value) { flight ->
                    FlightCard(flight)
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
    datePickerDialog.show()
}

@Composable
private fun FlightCard(flight: FlightInfo) {
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
                Text(text = flight.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = flight.date, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Seats Available: ${flight.seatsAvailable}", fontSize = 16.sp)
            Text(text = "Departure Location: ${flight.departureLocation}", fontSize = 16.sp)
            Text(text = "Departure Time: ${flight.departureTime}", fontSize = 16.sp)
            Text(text = "Destination Location: ${flight.destinationLocation}", fontSize = 16.sp)
            Text(text = "Destination Time: ${flight.destinationTime}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = flight.price, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Button(onClick = { /* Handle book now action */ }) {
                    Text(text = "Book now!")
                }
            }
        }
    }
}

fun sampleFlights(): List<FlightInfo> {
    return listOf(
        FlightInfo(
            name = "Sam Smith",
            date = "20-04-2024",
            seatsAvailable = "1/2",
            departureLocation = "VLC Airport",
            departureTime = "11:35Hrs",
            destinationLocation = "VLC Airport",
            destinationTime = "13:35Hrs",
            price = "€35"
        ),
        FlightInfo(
            name = "Jack Johnson",
            date = "20-04-2024",
            seatsAvailable = "2/2",
            departureLocation = "VLC Airport",
            departureTime = "16:05Hrs",
            destinationLocation = "VLC Airport",
            destinationTime = "18:25Hrs",
            price = "€45"
        ),
        FlightInfo(
            name = "Luke Like",
            date = "20-04-2024",
            seatsAvailable = "1/4",
            departureLocation = "VLC Airport",
            departureTime = "13:05Hrs",
            destinationLocation = "VLC Airport",
            destinationTime = "20:05Hrs",
            price = "€20"
        ),
        FlightInfo(
            name = "Rick Rock",
            date = "21-04-2024",
            seatsAvailable = "3/3",
            departureLocation = "VLC Airport",
            departureTime = "09:00Hrs",
            destinationLocation = "VLC Airport",
            destinationTime = "11:00Hrs",
            price = "€30"
        )
    )
}