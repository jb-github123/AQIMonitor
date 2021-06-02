package com.example.aqimonitor.helpers

import androidx.annotation.ColorRes
import com.example.aqimonitor.R
import com.example.aqimonitor.config.DEBUG_ON
import kotlin.math.roundToInt

object AQIGradeFormatter {

    private const val AQI_GRADE_UNKNOWN = 0
    private const val AQI_GRADE_GOOD = 1
    private const val AQI_GRADE_SATISFACTORY = 2
    private const val AQI_GRADE_MODERATE = 3
    private const val AQI_GRADE_POOR = 4
    private const val AQI_GRADE_VERY_POOR = 5
    private const val AQI_GRADE_SEVERE = 6

    /**
     * internal function to convert value to a AQI range code
     */
    private fun getAQIGrade(aqiValue: String): Int {
        try {
            val value = aqiValue.toFloat().roundToInt()
            return if (value in 1..50) {
                AQI_GRADE_GOOD
            } else if (value in 51..100) {
                AQI_GRADE_SATISFACTORY
            } else if (value in 101..200) {
                AQI_GRADE_MODERATE
            } else if (value in 201..300) {
                AQI_GRADE_POOR
            } else if (value in 301..400) {
                AQI_GRADE_VERY_POOR
            } else if (value in 401..500) {
                AQI_GRADE_SEVERE
            } else {
                AQI_GRADE_UNKNOWN
            }
        } catch (e: NumberFormatException) {
            if (DEBUG_ON) e.printStackTrace()
            return AQI_GRADE_UNKNOWN
        }
    }

    /**
     * Returns the color code for the aqiValue based on range groups
     */
    @ColorRes
    fun getAQIColorHighlights(aqiValue: String): Int {
        return when (getAQIGrade(aqiValue)) {
            AQI_GRADE_GOOD -> R.color.aqi_good
            AQI_GRADE_SATISFACTORY -> R.color.aqi_satisfactory
            AQI_GRADE_MODERATE -> R.color.aqi_moderate
            AQI_GRADE_POOR -> R.color.aqi_poor
            AQI_GRADE_VERY_POOR -> R.color.aqi_very_poor
            AQI_GRADE_SEVERE -> R.color.aqi_severe
            else -> R.color.aqi_unknown
        }
    }

    /**
     * Returns hint message for the aqiValue based on range groups
     */
    fun getAQIHintHighlights(aqiValue: String): String {
        return when (getAQIGrade(aqiValue)) {
            AQI_GRADE_GOOD -> "good"
            AQI_GRADE_SATISFACTORY -> "satisfactory"
            AQI_GRADE_MODERATE -> "moderate"
            AQI_GRADE_POOR -> "poor"
            AQI_GRADE_VERY_POOR -> "very poor"
            AQI_GRADE_SEVERE -> "severe"
            else -> "unknown"
        }
    }

}