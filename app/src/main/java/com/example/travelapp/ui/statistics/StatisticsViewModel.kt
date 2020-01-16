package com.example.travelapp.ui.statistics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatisticsViewModel(private val repository: TripsRepository) : ViewModel() {

    val trips = repository.allTrips

    val message = MutableLiveData<String>()

    fun clearAllData() = viewModelScope.launch(Dispatchers.IO) {
        repository.clearAllData()
        message.postValue("Done")
    }
}
