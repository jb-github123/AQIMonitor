package com.example.aqimonitor.ui.slideshow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aqimonitor.R

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = application.getString(R.string.text_about)

    }
    val text: LiveData<String> = _text
}