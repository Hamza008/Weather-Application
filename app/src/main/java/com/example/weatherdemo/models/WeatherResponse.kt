package com.example.weatherdemo.models

data class WeatherResponse(
    val name: String, // City name
    val main: Main,
    val weather: List<Weather>,
    val sys: Sys
)

data class Main(
    val temp: Double, // Temperature
    val humidity: Int // Humidity
)

data class Weather(
    val description: String, // Weather condition (e.g., clear sky)
    val icon: String // Weather icon
)

data class Sys(
    val country: String // Country code
)