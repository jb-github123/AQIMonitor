package com.example.aqimonitor.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class CityAQIData(
    // @PrimaryKey(autoGenerate = true) val id: Int,
    @PrimaryKey
    @ColumnInfo(name = "city_name") val cityName: String,
    @ColumnInfo(name = "current_aqi") val currentAQI: String,
    @ColumnInfo(name = "last_updated_timestamp")val lastUpdated: Long
)