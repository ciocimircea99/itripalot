package com.example.travelapp.ui.addedittrip

fun Float.priceToProgress(minPrice: Float, maxPrice: Float): Int {
    return (100 * this / (maxPrice - minPrice)).toInt()
}

fun Int.progressToPrice(minPrice: Float, maxPrice: Float): Float {
    return this * (maxPrice - minPrice) / 100
}