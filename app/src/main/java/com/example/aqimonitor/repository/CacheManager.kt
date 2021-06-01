package com.example.aqimonitor.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class CacheManager(context: Context) {

    private val cacheSharePrefFile = "cacheSharePrefFile"
    private val prefKeyServerCallTimestamp = "server_call_timestamp"
    private val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(cacheSharePrefFile, Context.MODE_PRIVATE)
    }

    private fun getLastServerCallTime(): Long =
        sharedPref.getLong(prefKeyServerCallTimestamp, System.nanoTime())

    fun setLastServerCallTime() {
        sharedPref.edit {
            this.putLong(prefKeyServerCallTimestamp, System.nanoTime())
            this.apply()
        }
    }

    fun isDataOutdated(): Boolean {
        // 1 second = 1000000000 nano seconds
        // val currentTime = System.nanoTime() / 1000000000 / 60 / 60 / 24 // value in days
        // val dbLastRefreshed = getLastServerCallTime() / 1000000000 / 60 / 60 / 24 // value in days

        return if (sharedPref.contains(prefKeyServerCallTimestamp)) {
            val currentTime = System.nanoTime() / 1000000000 / 60 // value in minutes
            val dbLastRefreshed = getLastServerCallTime() / 1000000000 / 60  // value in minutes
            (currentTime - dbLastRefreshed) >= 10 // do a refresh if data is older than 10 minutes
        } else {
            true // user launched app for the first time, shared prefs is not set
        }
    }

}