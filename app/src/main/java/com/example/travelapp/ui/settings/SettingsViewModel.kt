package com.example.travelapp.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: TripsRepository) : ViewModel() {

    val maxPrice = MutableLiveData<Float>()
    var newMaxPrice: Float = -1f

    val minPrice = MutableLiveData<Float>()
    var newMinPrice: Float = -1f

    val currency = MutableLiveData<String>()
    var newCurrency: String = ""

    val message = MutableLiveData<String>()

    init {
        maxPrice.value = repository.getMaxPrice()
        minPrice.value = repository.getMinPrice()
        currency.value = repository.getCurrency()
    }

    fun currencyChanged(currency: String) {
        newCurrency = currency
    }

    fun minPriceChanged(minPrice: Float) {
        newMinPrice = minPrice
    }

    fun maxPriceChanged(maxPrice: Float) {
        newMaxPrice = maxPrice
    }

    fun saveSettings() = viewModelScope.launch(Dispatchers.IO) {
        repository.saveCurrency(newCurrency)
        repository.saveMinPrice(newMinPrice)
        repository.saveMaxPrice(newMaxPrice)
        message.postValue("Done")
    }
}
