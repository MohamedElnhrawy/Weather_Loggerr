package com.example.weatherlogger.ui.details

import androidx.lifecycle.MutableLiveData
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.data.source.repository.WeatherRepository
import com.example.weatherlogger.ui.base.BaseViewModel
import com.example.weatherlogger.ui.home.HomeViewModel
import com.example.weatherlogger.ui.search.SearchViewModel
import com.example.weatherlogger.utils.asLiveData
import javax.inject.Inject


class DetailsViewModel @Inject constructor(private val repository: WeatherRepository) : HomeViewModel(repository){
    val currentWeather = MutableLiveData<Weather?>()


    override fun onCleared() {

        super.onCleared()
    }
}
