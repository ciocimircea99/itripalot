package com.example.travelapp.ui.addedittrip

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.model.Trip
import com.example.travelapp.model.TripType
import com.example.travelapp.repository.TripsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddEditTripViewModel(private val repository: TripsRepository) : ViewModel() {

    val tripMutableLive = MutableLiveData<Trip>()
    val messageMutableLive = MutableLiveData<String>()
    val actionFinished = MutableLiveData<Boolean>(false)

    val minPrice = repository.getMinPrice()
    val maxPrice = repository.getMaxPrice()

    fun initTrip(tripId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val trip = repository.getTrip(tripId)
            if (trip != null)
                tripMutableLive.postValue(trip)
            else
                tripMutableLive.postValue(Trip(price = (minPrice + maxPrice) / 2))
        }
    }

    fun nameChanged(name: String) {
        tripMutableLive.value?.name = name
    }

    fun destinationChanged(destination: String) {
        tripMutableLive.value?.destination = destination
    }

    fun setTripImage(imageBitmap: Bitmap) {
        tripMutableLive.value?.picture = imageBitmap
    }

    fun priceChanged(price: Float) {
        tripMutableLive.value?.price = price
    }

    fun endDateChanged(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        tripMutableLive.value?.endDate = LocalDate.of(year, monthOfYear, dayOfMonth)
    }

    fun startDateChanged(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        tripMutableLive.value?.startDate = LocalDate.of(year, monthOfYear, dayOfMonth)
    }

    fun setTripType(tripType: TripType) {
        tripMutableLive.value?.type = tripType
    }

    fun ratingChanged(rating: Float) {
        tripMutableLive.value?.rating = rating
    }

    fun upsertTrip() {
        viewModelScope.launch(Dispatchers.IO) {
            val trip = tripMutableLive.value ?: return@launch
            repository.upsertTrip(trip)
            actionFinished.postValue(true)
        }
    }

    fun deleteTrip() {
        viewModelScope.launch(Dispatchers.IO) {
            val trip = tripMutableLive.value ?: return@launch
            repository.deleteTrip(trip)
            actionFinished.postValue(true)
        }
    }
}
