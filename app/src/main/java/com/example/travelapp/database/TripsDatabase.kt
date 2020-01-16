package com.example.travelapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.travelapp.model.Trip

@Database(entities = [Trip::class], version = 18, exportSchema = false)
@TypeConverters(DataTypeConverter::class)
abstract class TripsDatabase : RoomDatabase() {

    abstract fun tripsDao(): TripsDao

    companion object {
        @Volatile
        private var INSTANCE: TripsDatabase? = null

        fun getDatabase(context: Context): TripsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripsDatabase::class.java,
                    "trips_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}