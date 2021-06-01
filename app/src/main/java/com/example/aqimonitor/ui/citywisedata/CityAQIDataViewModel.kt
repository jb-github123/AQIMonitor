package com.example.aqimonitor.ui.citywisedata

import android.app.Application
import androidx.lifecycle.*
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.repository.Repository
import kotlinx.coroutines.launch

class CityAQIDataViewModel(application: Application) : AndroidViewModel(application) {

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }*/

    private val _cityAQIData = MutableLiveData<CityAQIData>()
    val cityAQIData: LiveData<CityAQIData> = _cityAQIData

    fun setCityAQIData(cityAQIData: CityAQIData){
        _cityAQIData.value = cityAQIData
    }

    private val repository: Repository = Repository(application)

    fun getCityAQIDataFromDB(): LiveData<List<CityAQIData>> {
        return repository.getCityAQIDataFromDB(_cityAQIData.value?.cityName.toString())
    }

    fun refreshCityAQIData(){
        viewModelScope.launch {
            repository.refreshCityAQIData()
        }
    }

    fun stopCityAQIDataUpdates(){
        repository.stopAQIDataUpdatesFromServer()
    }

}