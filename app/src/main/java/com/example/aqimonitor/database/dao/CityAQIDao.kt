package com.example.aqimonitor.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aqimonitor.database.model.CityAQIData

@Dao
interface CityAQIDao {

    @Query("select * from cityaqidata group by city_name order by city_name")
    fun getAllCityAQIData(): LiveData<List<CityAQIData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCityAQIData(cityAQIData: CityAQIData)

    @Query("select * from cityaqidata where city_name = :cityName order by last_updated_timestamp desc")
    fun getCityAQIData(cityName: String): LiveData<List<CityAQIData>>

}