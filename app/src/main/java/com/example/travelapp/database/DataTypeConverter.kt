package com.example.travelapp.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.example.travelapp.model.TripType
import java.io.ByteArrayOutputStream
import java.time.LocalDate

class DataTypeConverter {

    @TypeConverter
    fun tripTypeToString(tripType: TripType): String {
        return when (tripType) {
            TripType.CITY_BREAK -> "citybreak"
            TripType.MOUNTAIN -> "mountain"
            TripType.SEA_SIDE -> "seaside"
            else -> ""
        }
    }

    @TypeConverter
    fun stringToTripType(string: String): TripType {
        return when (string) {
            "citybreak" -> TripType.CITY_BREAK
            "mountain" -> TripType.MOUNTAIN
            "seaside" -> TripType.SEA_SIDE
            else -> TripType.NO_TYPE
        }
    }

    @TypeConverter
    fun timestampToLocalDate(timestamp: Long): LocalDate {
        return LocalDate.ofEpochDay(timestamp)
    }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate): Long {
        return date.toEpochDay()
    }
}