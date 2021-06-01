package com.example.aqimonitor.network

import okhttp3.OkHttpClient

class NetworkManager {

    companion object {

        val BASE_URL = "ws://city-ws.herokuapp.com/"
        val SOCKET_NORMAL_CLOSE = 1000

        @Volatile
        private var networkClient: OkHttpClient? = null

        fun getInstance(): OkHttpClient {
            return networkClient ?: synchronized(this) {
                val instance = OkHttpClient()
                networkClient = instance
                instance
            }
        }

    }

}