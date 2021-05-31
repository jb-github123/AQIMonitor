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

    fun setLastServerCallTime(serverCallTimestamp: Long) {
        sharedPref.edit {
            this.putLong(prefKeyServerCallTimestamp, serverCallTimestamp)
            this.apply()
        }
    }

    fun getLastServerCallTime(): Long = sharedPref.getLong(prefKeyServerCallTimestamp, 0)

}