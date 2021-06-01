package com.example.aqimonitor.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aqimonitor.database.model.CityAQIData

@Dao
interface CityAQIDao {

    @Query("select * from cityaqidata")
    fun getAllCityAQIDao(): LiveData<List<CityAQIData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCityAQIData(cityAQIData: CityAQIData)

}