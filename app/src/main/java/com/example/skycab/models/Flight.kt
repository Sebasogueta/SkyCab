package com.example.skycab.models

import java.util.Date
import java.time.LocalDateTime

data class Flight(
    val flightId: String,
    val pilotId: String,
    val passengers: MutableList<User>,
    val totalSeats: Int,
    val departureDateTime: String,
    val arrivalDateTime: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val publicationDate: String,
    val price: Double,
    val ended: Boolean // diferenciar entre próximos vuelos (false), y los vuelos pasados(true).
    ){
    constructor() : this("", "",mutableListOf(), 0, LocalDateTime.now().toString(), LocalDateTime.now().toString(),"", "", Date().toString(), 0.0, false)

}
