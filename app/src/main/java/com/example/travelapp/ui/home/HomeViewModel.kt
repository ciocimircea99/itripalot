package com.example.travelapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.model.Trip
import com.example.travelapp.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: TripsRepository) : ViewModel() {

    val trips = repository.allTrips

    fun updateTrip(trip: Trip) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsertTrip(trip)
        }
    }
}