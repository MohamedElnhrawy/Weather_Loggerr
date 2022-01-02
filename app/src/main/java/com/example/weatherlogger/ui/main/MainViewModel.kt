package com.example.weatherlogger.ui.main

import com.example.weatherlogger.data.source.repository.WeatherRepository
import com.example.weatherlogger.ui.base.BaseViewModel
import javax.inject.Inject


class MainViewModel @Inject constructor(private val repository: WeatherRepository) : BaseViewModel(repository){
    init {

    }

    override fun onCleared() {

        super.onCleared()
    }
}
