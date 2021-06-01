package com.example.aqimonitor.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aqimonitor.database.dao.CityAQIDao
import com.example.aqimonitor.database.model.CityAQIData

@Database(entities = [CityAQIData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCityAQIDataDao(): CityAQIDao

    companion object {

        @Volatile
        private var db: AppDatabase? = null

        fun getDatabaseInstance(application: Application): AppDatabase {
            return db ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        application,
                        AppDatabase::class.java,
                        "aqiDB"
                    ).build()
                db = instance
                instance
            }
        }

    }

}