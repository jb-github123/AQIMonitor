package com.example.aqimonitor.ui.citywisedata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqimonitor.R
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.helpers.AQIGradeFormatter
import com.example.aqimonitor.helpers.StringFormatter
import com.example.aqimonitor.ui.citywisedata.adapter.CityAQIDataListAdapter

class CityAQIDataFragment : Fragment() {

    private lateinit var cityAQIDataViewModel: CityAQIDataViewModel
    private lateinit var recyclerViewAQIData: RecyclerView
    private val aqiDataList = ArrayList<CityAQIData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cityAQIDataViewModel = ViewModelProvider(requireActivity()).get(CityAQIDataViewModel::class.java)
    }

    /*override fun onResume() {
        super.onResume()
        cityAQIDataViewModel.refreshCityAQIData()
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_city_aqi_data, container, false)

        val cityListAdapter = CityAQIDataListAdapter(aqiDataList)
        recyclerViewAQIData = root.findViewById(R.id.recyclerViewAQIData)
        recyclerViewAQIData.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerViewAQIData.adapter = cityListAdapter

        val textViewCityName: TextView = root.findViewById(R.id.text_city_name)
        val textViewCurrentAQI: TextView = root.findViewById(R.id.text_city_aqi)
        cityAQIDataViewModel.cityAQIData.observe(viewLifecycleOwner, Observer {

            val aqiValue =
                StringFormatter.getDoubleFormatterWithTwoDecimals(it.currentAQI.toDouble())

            textViewCityName.text = it.cityName
            textViewCurrentAQI.text = aqiValue

            textViewCurrentAQI.setTextColor(
                resources.getColor(
                    AQIGradeFormatter.getAQIColorHighlights(
                        it.currentAQI
                    )
                )
            )

        })

        cityAQIDataViewModel.getCityAQIDataFromDB().observe(viewLifecycleOwner, Observer {
            aqiDataList.clear()
            aqiDataList.addAll(it)
            recyclerViewAQIData.adapter?.notifyDataSetChanged()
        })

        return root
    }

    /*override fun onPause() {
        super.onPause()
        cityAQIDataViewModel.stopCityAQIDataUpdates()
    }*/

}