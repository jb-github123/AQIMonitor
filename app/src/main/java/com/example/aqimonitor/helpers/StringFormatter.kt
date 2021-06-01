package com.example.aqimonitor.helpers

import java.text.DecimalFormat

object StringFormatter {

    fun getDoubleFormatterWithTwoDecimals(value: Double): String {
        return DecimalFormat("#.00").format(value)
    }

    fun getDoubleFormatterWithTwoDecimalsV2(value: Double): String {
        return String.format("%.2f", value)
    }

}