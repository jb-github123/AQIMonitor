package com.example.aqimonitor.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqimonitor.R
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.ui.home.adapter.CityListAdapter

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var recyclerViewCities: RecyclerView
    private val cityList = ArrayList<CityAQIData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
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

        val cityListAdapter = CityListAdapter(cityList)
        recyclerViewCities = root.findViewById(R.id.recyclerViewCities)
        recyclerViewCities.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerViewCities.adapter = cityListAdapter

        homeViewModel.getCityAQIDataFromDB().observe(viewLifecycleOwner, Observer {
            cityList.clear()
            cityList.addAll(it)
        })

        homeViewModel.refreshCityAQIData()

        return root
    }
}