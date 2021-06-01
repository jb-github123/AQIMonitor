package com.example.aqimonitor.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.aqimonitor.config.DEBUG_ON
import com.example.aqimonitor.database.AppDatabase
import com.example.aqimonitor.database.dao.CityAQIDao
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.network.NetworkManager
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class Repository(private val application: Application) {

    private val NETWORK_RESPONSE_TAG = "NETWORK_RESPONSE_TAG"

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

        val listener: WebSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.e(NETWORK_RESPONSE_TAG, "WebSocket opened - $response")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.e(NETWORK_RESPONSE_TAG, "WebSocket response - $text")

                CacheManager(application).setLastServerCallTime()

                // todo loop
                // newCityAQIDataList.add()
                // loop

                // todo save data in db
                // saveAQIDataInDb()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                if (DEBUG_ON) {
                    if (DEBUG_ON) Log.e(NETWORK_RESPONSE_TAG, "WebSocket Failure - $response")
                    t.printStackTrace()
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                if (DEBUG_ON) Log.e(NETWORK_RESPONSE_TAG, "WebSocket Closing - $code - $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                if (DEBUG_ON) Log.e(NETWORK_RESPONSE_TAG, "WebSocket Closed - $code - $reason")
            }

        }

        val request: Request = Request.Builder().url(NetworkManager.BASE_URL).build()

        // todo create a socket connection and get data from server, close connection, store data in db
        val webSocket = NetworkManager.getInstance().newWebSocket(request, listener)
        NetworkManager.getInstance().dispatcher.executorService.shutdown()
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