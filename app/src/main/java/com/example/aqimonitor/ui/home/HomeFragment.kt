package com.example.aqimonitor.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqimonitor.R
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.ui.citywisedata.CityAQIDataViewModel
import com.example.aqimonitor.ui.home.adapter.CityListAdapter

class HomeFragment : Fragment(), CityListAdapter.CitySelectedListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerViewCities: RecyclerView
    private val cityList = ArrayList<CityAQIData>()

    private lateinit var cityAQIDataViewModel: CityAQIDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        cityAQIDataViewModel = ViewModelProvider(requireActivity()).get(CityAQIDataViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.refreshCityAQIData()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        /*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        val cityListAdapter = CityListAdapter(cityList, this)
        recyclerViewCities = root.findViewById(R.id.recyclerViewCities)
        recyclerViewCities.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerViewCities.adapter = cityListAdapter

        homeViewModel.getCityAQIDataFromDB().observe(viewLifecycleOwner, Observer {
            cityList.clear()
            cityList.addAll(it)
            recyclerViewCities.adapter?.notifyDataSetChanged()
        })

        return root
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.stopCityAQIDataUpdates()
    }

    override fun onCitySelected(cityAQIData: CityAQIData) {
        cityAQIDataViewModel.setCityAQIData(cityAQIData)
        findNavController().navigate(R.id.action_nav_home_to_nav_city_aqi_data)
    }
}