package com.example.travelapp.provider

import android.content.Context


class SharedPrefProvider(context: Context) {
    companion object {
        const val MIN_PRICE_KEY = "minPrice"
        const val MAX_PRICE_KEY = "maxPrice"
        const val CURRENCY = "currency"
    }

    private val sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)

    fun saveMaxPrice(maxPrice: Float) {
        sharedPref.edit().putFloat(MAX_PRICE_KEY, maxPrice).apply()
    }

    fun saveMinPrice(maxPrice: Float) {
        sharedPref.edit().putFloat(MAX_PRICE_KEY, maxPrice).apply()
    }

    fun saveCurrency(currency: String) {
        sharedPref.edit().putString(CURRENCY, currency).apply()
    }

    fun loadMaxPrice(): Float {
        return sharedPref.getFloat(MAX_PRICE_KEY, 1000f)
    }

    fun loadMinPrice(): Float {
        return sharedPref.getFloat(MIN_PRICE_KEY, 0f)
    }

    fun loadCurrency(): String {
        return sharedPref.getString(CURRENCY, "$").toString()
    }
}