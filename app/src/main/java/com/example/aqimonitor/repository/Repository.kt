package com.example.aqimonitor.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.aqimonitor.database.AppDatabase
import com.example.aqimonitor.database.dao.CityAQIDao
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.network.NetworkManager
import com.example.aqimonitor.network.model.AQIDataResponse
import com.example.aqimonitor.network.websocketlistener.SingleResponseWebSocketListener
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Request

class Repository(private val application: Application) :
    SingleResponseWebSocketListener.OnSingleMessageReceivedListener {

    private val TAG = Repository::class.java.simpleName

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
    suspend fun refreshCityAQIData() {
        if (CacheManager(application).isDataOutdated()) {
            getAQIDataFromServer()
        }
    }

    /**
     * get data from server via api call
     */
    private suspend fun getAQIDataFromServer() {
        // create a socket connection and get data from server, close connection, store data in db
        val request: Request = Request.Builder().url(NetworkManager.BASE_URL).build()
        val webSocket = NetworkManager.getInstance().newWebSocket(request, SingleResponseWebSocketListener(this))
        NetworkManager.getInstance().dispatcher.executorService.shutdown()
    }

    override fun onSingleAQIDataReceived(jsonString: String) {
        CacheManager(application).setLastServerCallTime()

        /* val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<AQIDataResponse> = moshi.adapter(AQIDataResponse::class.java)
        val AQIDataResponseJSON: AQIDataResponse? = jsonAdapter.fromJson(text)
        System.out.println(AQIDataResponseJSON) */

        // todo save data in db
        GlobalScope.launch(Dispatchers.IO) {
            saveAQIDataInDb(jsonString)
        }

    }

    /**
     * save recently pulled data into db,
     * this will trigger any observers on <code>getAQIDataFromDB()</code> method to update ui
     */
    private suspend fun saveAQIDataInDb(jsonString: String) {
        val gson: Gson = Gson()
        // val AQIDataResponseJSON: AQIDataResponse = gson.fromJson(text, AQIDataResponse::class.java)
        val aqiDataResponseJSON: Array<AQIDataResponse> = gson.fromJson(
            jsonString,
            Array<AQIDataResponse>::class.java
        )

        // loop
        newCityAQIDataList.clear()
        var cityAQIData: CityAQIData
        for (aqiCityData in aqiDataResponseJSON) {
            cityAQIData = CityAQIData(
                0,
                aqiCityData.city,
                aqiCityData.aqi,
                System.currentTimeMillis()
            )
            newCityAQIDataList.add(cityAQIData)
            Log.e(TAG, "city ${cityAQIData.cityName} - ${cityAQIData.currentAQI} - ${cityAQIData.lastUpdated}")
        }
        // loop

        for (newCityAQIData in newCityAQIDataList) {
            cityAQIDao.addCityAQIData(newCityAQIData)
        }
    }

    /**
     * return data stored in db, set observers on this method to check for data changes
     */
    fun getAQIDataFromDB(): LiveData<List<CityAQIData>> = cityAQIDao.getAllCityAQIDao()

}