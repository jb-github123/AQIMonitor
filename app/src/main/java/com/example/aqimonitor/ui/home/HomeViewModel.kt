package com.example.aqimonitor.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text*/

    private val repository: Repository = Repository(application)

    /*private val cityList = MutableLiveData<List<CityAQIData>>()
    val cityListLiveData = cityList*/

    fun getCityAQIDataFromDB(): LiveData<List<CityAQIData>> {
        return repository.getAQIDataFromDB()
    }

    fun refreshCityAQIData(){
        viewModelScope.launch {
            repository.refreshCityAQIData()
        }
    }

}