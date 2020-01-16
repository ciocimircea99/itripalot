package com.example.travelapp

import android.app.Application
import com.example.travelapp.database.TripsDatabase
import com.example.travelapp.networking.WeatherApiService
import com.example.travelapp.provider.InternalStorageProvider
import com.example.travelapp.provider.SharedPrefProvider
import com.example.travelapp.repository.TripsRepository
import com.example.travelapp.ui.ViewModelFactory

class TravelApp : Application() {

    private lateinit var tripsDatabase: TripsDatabase
    private lateinit var sharedPref: SharedPrefProvider
    private lateinit var internalStorageProvider: InternalStorageProvider
    private lateinit var repository: TripsRepository
    private lateinit var weatherApiService: WeatherApiService

    private lateinit var viewModelFactory: ViewModelFactory


    override fun onCreate() {
        super.onCreate()
        tripsDatabase = TripsDatabase.getDatabase(applicationContext)

        sharedPref = SharedPrefProvider(applicationContext)

        internalStorageProvider = InternalStorageProvider(applicationContext)

        repository = TripsRepository(tripsDatabase.tripsDao(), sharedPref, internalStorageProvider)

        weatherApiService = WeatherApiService.invoke()

        viewModelFactory = ViewModelFactory(repository, weatherApiService)
    }

    fun getViewModelFactory(): ViewModelFactory {
        return viewModelFactory
    }
}