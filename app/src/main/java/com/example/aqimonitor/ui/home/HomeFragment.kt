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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        /*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        val list = ArrayList<CityAQIData>()
        list.add(CityAQIData("Mumbai", "55.5555", "Today"))
        list.add(CityAQIData("Delhi", "44.4444", "Few Minutes Ago"))
        list.add(CityAQIData("New York City", "33.3333", "Few Hours Ago"))
        list.add(CityAQIData("San Jose", "22.2222", "Yesterday"))

        val cityListAdapter = CityListAdapter(list)
        recyclerViewCities = root.findViewById(R.id.recyclerViewCities)
        recyclerViewCities.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerViewCities.adapter = cityListAdapter

        return root
    }
}