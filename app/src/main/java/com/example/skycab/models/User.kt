package com.example.skycab.models

data class User(
    val username: String,
    val pilotLicense: String,
    val bio: String,
    val flightIds: MutableList<String>,
    val pilotFlightIds: MutableList<String>
) {
    // Constructor sin argumentos (necesario para Firestore)
    constructor() : this("", "","", mutableListOf(), mutableListOf())
}