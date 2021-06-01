package com.example.aqimonitor.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.aqimonitor.database.AppDatabase
import com.example.aqimonitor.database.dao.CityAQIDao
import com.example.aqimonitor.database.model.CityAQIData

class Repository(private val application: Application) {

    private var cityAQIDao: CityAQIDao
    private val newCityAQIDataList = ArrayList<CityAQIData>()

    init {
        // get db instance
        val dbInstance = AppDatabase.getDatabaseInstance(application)
        cityAQIDao = dbInstance.getCityAQIDataDao()
    }

    /**
     * decide if data needs to be refreshed in db from server, if required get updated data and
     * store it in db
     */
    fun refreshCityAQIData() {
        if (CacheManager(application).isDataOutdated()) {
            getAQIDataFromServer()
        }
    }

    /**
     * get data from server via api call
     */
    private fun getAQIDataFromServer() {
        // todo create a socket connection and get data from server, close connection, store data in db
        /*on success {
            CacheManager(application).setLastServerCallTime()

            // loop
            // newCityAQIDataList.add()
            // loop

            saveAQIDataInDb()
        }*/
    }

    /**
     * save recently pulled data into db,
     * this will trigger any observers on <code>getAQIDataFromDB()</code> method to update ui
     */
    private suspend fun saveAQIDataInDb() {
        for (newCityAQIData in newCityAQIDataList) {
            cityAQIDao.addCityAQIData(newCityAQIData)
        }
    }

    /**
     * return data stored in db, set observers on this method to check for data changes
     */
    fun getAQIDataFromDB(): LiveData<List<CityAQIData>> = cityAQIDao.getAllCityAQIDao()

}