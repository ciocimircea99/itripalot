package com.example.travelapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelapp.networking.WeatherApiService
import com.example.travelapp.repository.TripsRepository
import com.example.travelapp.ui.addedittrip.AddEditTripViewModel
import com.example.travelapp.ui.details.TripDetailsViewModel
import com.example.travelapp.ui.home.HomeViewModel
import com.example.travelapp.ui.settings.SettingsViewModel
import com.example.travelapp.ui.statistics.StatisticsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: TripsRepository,
    private val weatherApiService: WeatherApiService
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass.simpleName) {
            HomeViewModel::class.java.simpleName -> HomeViewModel(repository)
            AddEditTripViewModel::class.java.simpleName -> AddEditTripViewModel(repository)
            TripDetailsViewModel::class.java.simpleName -> TripDetailsViewModel(
                repository,
                weatherApiService
            )
            StatisticsViewModel::class.java.simpleName -> StatisticsViewModel(repository)
            SettingsViewModel::class.java.simpleName -> SettingsViewModel(repository)
            else -> null
        } as T
    }
}