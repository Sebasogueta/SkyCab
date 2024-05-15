package com.example.skycab.models

import java.sql.Date
import java.time.LocalDateTime

data class Flight(
    val flightId: Int,
    val pilotUsername: String,
    val passengers: MutableList<User>,
    val totalSeats: Int,
    val departureDateTime: LocalDateTime,
    val arrivalDateTime: LocalDateTime,
    val departureAirport: String,
    val arrivalAirport: String,
    val publicationDate: Date,
    val ended: Boolean // diferenciar entre próximos vuelos (false), y los vuelos pasados(true).
    )
