package com.example.travelapp.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "trips")
data class Trip(
    var name: String = "",
    var destination: String = "",
    var type: TripType = TripType.NO_TYPE,
    var startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.now(),
    var price: Float = 200f,
    var rating: Float = 2.5f,
    var favourite: Boolean = false,
    @Ignore
    var currency: String = "",
    @Ignore
    var picture: Bitmap? = null,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
