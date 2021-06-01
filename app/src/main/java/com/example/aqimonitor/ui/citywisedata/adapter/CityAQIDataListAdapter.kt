package com.example.aqimonitor.ui.citywisedata.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aqimonitor.R
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.helpers.AQIGradeFormatter
import com.example.aqimonitor.helpers.DateFormatter
import com.example.aqimonitor.helpers.StringFormatter

class CityAQIDataListAdapter(
    private val list: ArrayList<CityAQIData>
) : RecyclerView.Adapter<CityAQIDataListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewHintMessage: TextView = itemView.findViewById(R.id.textViewHintMessage)
        val textViewCurrentAQI: TextView = itemView.findViewById(R.id.textViewCurrentAQI)
        val textViewLastUpdated: TextView = itemView.findViewById(R.id.textViewLastUpdated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_row_city_aqi_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aqiValue =
            StringFormatter.getDoubleFormatterWithTwoDecimals(list[position].currentAQI.toDouble())

        holder.textViewHintMessage.text = holder.itemView.context.getString(
            R.string.text_air_quality_was,
            AQIGradeFormatter.getAQIHintHighlights(aqiValue)
        )
        holder.textViewCurrentAQI.text = aqiValue
        holder.textViewCurrentAQI.setTextColor(
            holder.itemView.context.resources.getColor(
                AQIGradeFormatter.getAQIColorHighlights(
                    aqiValue
                )
            )
        )
        holder.textViewLastUpdated.text = holder.itemView.context.getString(
            R.string.text_last_updated_single_line_placeholder,
            DateFormatter.getFormattedTimestamp(list[position].lastUpdated)
        )
    }

    override fun getItemCount(): Int = list.size

}