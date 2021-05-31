package com.example.aqimonitor.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.aqimonitor.database.model.CityAQIData

class Repository(val application: Application) {

    init {
        // get db instance
    }

    private fun saveAQIDataInDb() {
        // todo save data in db
    }

    private fun getAQIDataFromServer() {
        // todo create a socket connection and get data from server, close connection, store data in db
        /*on success {
            CacheManager(application).setLastServerCallTime(System.nanoTime())
            saveAQIDataInDb()
        }*/

    }

    fun getAQIDataFromDB(): LiveData<List<CityAQIData>> {
        // todo return data stored in db
    }

    fun refreshCityAQIData() {
        // todo decide if data to be returned from db or from server, implement caching logic
        if (System.nanoTime() - CacheManager(application).getLastServerCallTime() < 0) {
            getAQIDataFromServer()
        }
    }

}