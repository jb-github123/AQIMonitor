package com.example.aqimonitor.helpers

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    private const val dateTimeFormatPattern = "dd MMM, yyyy hh:mm a"
    private val simpleDateFormat = SimpleDateFormat(dateTimeFormatPattern, Locale.ENGLISH)

    fun getFormattedTimestamp(timeStamp: Long): String {
        return simpleDateFormat.format(timeStamp)
    }

}