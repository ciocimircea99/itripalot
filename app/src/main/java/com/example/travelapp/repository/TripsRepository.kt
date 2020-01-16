package com.example.travelapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.travelapp.database.TripsDao
import com.example.travelapp.model.Trip
import com.example.travelapp.provider.InternalStorageProvider
import com.example.travelapp.provider.SharedPrefProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripsRepository(
    private val tripsDao: TripsDao,
    private val sharedPrefProvider: SharedPrefProvider,
    private val internalStorageProvider: InternalStorageProvider
) {

    private val repositoryCoroutineScope = CoroutineScope(Dispatchers.IO)

    val allTrips = MutableLiveData<List<Trip>>()

    init {
        tripsDao.getTrips().observeForever { trips ->
            repositoryCoroutineScope.launch {
                val tripsToDisplay = trips.map { trip ->

                    val tripPicture = withContext(Dispatchers.IO) {
                        internalStorageProvider.loadBitmap(trip.id.toString())
                    }

                    trip.picture = tripPicture
                    trip.currency = sharedPrefProvider.loadCurrency()
                    trip
                }
                allTrips.postValue(tripsToDisplay)
            }
        }
    }

    suspend fun getTrip(tripId: Long): Trip? {
        return if (tripId != -1L) {
            val trip = tripsDao.getTrip(tripId)
            val tripPicture =
                withContext(Dispatchers.IO) {
                    internalStorageProvider.loadBitmap(trip.id.toString())
                }
            trip.picture = tripPicture
            trip.currency = sharedPrefProvider.loadCurrency()
            trip
        } else {
            null
        }
    }

    suspend fun upsertTrip(trip: Trip) {
        val tripPicture = trip.picture
        if (tripPicture != null) {
            withContext(Dispatchers.IO) {
                val tempFileName = "temp"
                internalStorageProvider.saveBitmap(
                    tripPicture,
                    tempFileName
                )
                val insertedId = tripsDao.insertTrip(trip)
                internalStorageProvider.renameFile(tempFileName, insertedId.toString())
            }
        }
    }

    suspend fun deleteTrip(trip: Trip) {
        tripsDao.deleteTrip(trip)
        withContext(Dispatchers.IO) {
            internalStorageProvider.deleteBitmap(trip.id.toString())
        }
    }

    suspend fun clearAllData() {
        val allTrips = allTrips.value ?: return
        allTrips.forEach { trip ->
            deleteTrip(trip)
        }
    }

    fun getCurrency(): String {
        return sharedPrefProvider.loadCurrency()
    }

    fun getMinPrice(): Float {
        return sharedPrefProvider.loadMinPrice()
    }

    fun getMaxPrice(): Float {
        return sharedPrefProvider.loadMaxPrice()
    }

    fun saveCurrency(s: String) {
        sharedPrefProvider.saveCurrency(s)
    }

    fun saveMinPrice(value: Float) {
        sharedPrefProvider.saveMinPrice(value)
    }

    fun saveMaxPrice(value: Float) {
        sharedPrefProvider.saveMaxPrice(value)
    }
}