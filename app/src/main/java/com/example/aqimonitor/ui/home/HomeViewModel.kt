package com.example.aqimonitor.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.example.aqimonitor.database.model.CityAQIData
import com.example.aqimonitor.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) :
    AndroidViewModel(application),
    Repository.RepositoryResponseListener {

    /*private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text*/

    private val repository: Repository = Repository(application)

    private val _isConnectionLost = MutableLiveData<Boolean>()
    val isConnectionLost: LiveData<Boolean> = _isConnectionLost

    init {
        repository.repositoryResponseListener = this
    }

    fun getCityAQIDataFromDB(): LiveData<List<CityAQIData>> {
        return repository.getAQIDataFromDB()
    }

    fun refreshCityAQIData(){
        _isConnectionLost.value = false
        viewModelScope.launch {
            repository.refreshCityAQIData()
        }
    }

    fun stopCityAQIDataUpdates(){
        repository.stopAQIDataUpdatesFromServer()
    }

    override fun onConnectionFailure() {
        _isConnectionLost.value = true
    }

}