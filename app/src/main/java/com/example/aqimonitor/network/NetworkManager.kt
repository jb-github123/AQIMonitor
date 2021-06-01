package com.example.aqimonitor.network

import okhttp3.OkHttpClient

class NetworkManager {

    companion object {

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