package com.example.travelapp.networking

import android.annotation.SuppressLint

fun getWeatherIconURL(iconCode: String): String {
    return "http://openweathermap.org/img/w/$iconCode.png"
}

fun formatHumidity(humidity: Int): String {
    return "Humidity: $humidity%"
}

fun formatTemperature(temperature: Double): String {
    //(32°F − 32) × 5/9 = 0°C
    return "Temperature: ${((temperature - 32) * 5 / 9).format(2)}°C"
}

@SuppressLint("DefaultLocale")
fun formatDescription(description: String): String {
    return description.capitalize()
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)