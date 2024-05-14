package com.example.skycab.models

data class User(
    val username: String,
    //val userFlights: MutableList<Flight>,
    val pilotLicense: String
    //val favorites: MutableList<String>
) {
    // Constructor sin argumentos (necesario para Firestore)
    //constructor() : this("", mutableListOf(), "")
    constructor() : this("", "")
}