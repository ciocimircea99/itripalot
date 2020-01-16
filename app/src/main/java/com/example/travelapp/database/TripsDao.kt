package com.example.travelapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.travelapp.model.Trip

@Dao
interface TripsDao {

    @Query("SELECT * from trips")
    fun getTrips(): LiveData<List<Trip>>

    @Query("SELECT * FROM trips WHERE id =:tripId")
    fun getTrip(tripId: Long): Trip

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrip(trip: Trip): Long

    @Delete
    fun deleteTrip(trip: Trip)
}