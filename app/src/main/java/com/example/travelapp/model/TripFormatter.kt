package com.example.travelapp.model

fun Float.formatPrice(currency: String): String {
    return "${this.toInt()} $currency"
}