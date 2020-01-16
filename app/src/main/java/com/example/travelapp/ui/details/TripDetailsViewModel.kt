package com.example.travelapp.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.model.Trip
import com.example.travelapp.networking.WeatherApiService
import com.example.travelapp.networking.response.CurrentWeatherResponse
import com.example.travelapp.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripDetailsViewModel(
    private val repository: TripsRepository,
    private val weatherApiService: WeatherApiService
) : ViewModel() {

    val tripMutableLive = MutableLiveData<Trip>()
    val weatherMutableLive = MutableLiveData<CurrentWeatherResponse>()


    fun initTrip(tripId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val trip = repository.getTrip(tripId)
        if (trip != null) {
            tripMutableLive.postValue(trip)
            fetchWeather(trip.destination)
        }
    }

    private fun fetchWeather(location: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherResponse: CurrentWeatherResponse? = try {
                weatherApiService.getWeather(location).await()
            } catch (e: Exception) {
                null
            }
            weatherMutableLive.postValue(weatherResponse)
        }
    }
}