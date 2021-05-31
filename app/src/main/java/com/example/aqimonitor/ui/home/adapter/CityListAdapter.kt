package com.example.aqimonitor.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aqimonitor.R
import com.example.aqimonitor.database.model.CityAQIData

class CityListAdapter(private val list: ArrayList<CityAQIData>) :
    RecyclerView.Adapter<CityListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCity: TextView = itemView.findViewById(R.id.textViewCity)
        val textViewCurrentAQI: TextView = itemView.findViewById(R.id.textViewCurrentAQI)
        val textViewLastUpdated: TextView = itemView.findViewById(R.id.textViewLastUpdated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_row_city_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewCity.text = list[position].cityName
        holder.textViewCurrentAQI.text = list[position].currentAQI
        holder.textViewLastUpdated.text = list[position].lastUpdated
    }

    override fun getItemCount(): Int = list.size

}