package com.example.skycab.models

import java.util.Date
import java.time.LocalDateTime

data class Flight(
    val flightId: String,
    val pilotId: String,
    val passengers: MutableList<String>,
    val totalSeats: Int,
    val departureDateTime: String,
    val arrivalDateTime: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val publicationDate: String,
    val ended: Boolean, // diferenciar entre próximos vuelos (false), y los vuelos pasados(true).
    val price: Double
    ){
    constructor() : this("", "",mutableListOf(), 0, LocalDateTime.now().toString(), LocalDateTime.now().toString(),"", "", Date().toString(), false,0.0,)

}
