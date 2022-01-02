package com.example.weatherlogger.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.data.source.repository.WeatherRepository
import com.example.weatherlogger.ui.base.BaseViewModel
import com.example.weatherlogger.utils.AppUtils.Companion.currentSystemTime
import com.example.weatherlogger.utils.LocationLiveData
import com.example.weatherlogger.utils.asLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.weatherlogger.utils.Result.*
import com.example.weatherlogger.utils.convertKelvinToCelsius


open class HomeViewModel @Inject constructor(private val repository: WeatherRepository) : BaseViewModel(repository){
    @Inject
    lateinit var locationLiveData: LocationLiveData
    val time = currentSystemTime()
    fun fetchLocationLiveData() = locationLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _dataFetchState = MutableLiveData<Boolean>()
    val dataFetchState = _dataFetchState.asLiveData()

    private val _weather = MutableLiveData<Weather?>()
    val weather = _weather.asLiveData()


    init {

    }


    fun getWeather(location: LocationModel) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            when (val result = repository.getWeather(location, false)) {
                is Success -> {
                    _isLoading.value = false
                    if (result.data != null) {
                        val weather = result.data
                        _dataFetchState.value = true
                        _weather.value = weather
                    } else {
                        refreshWeather(location)
                    }
                }
                is Error -> {
                    _isLoading.value = false
                    _dataFetchState.value = false
                }

                is Loading -> _isLoading.postValue(true)
            }
        }
    }
    fun refreshWeather(location: LocationModel) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.getWeather(location, true)) {
                is Success -> {
                    _isLoading.value = false
                    if (result.data != null) {
                        val weather = result.data.apply {
                            this.networkWeatherCondition.temp = convertKelvinToCelsius(this.networkWeatherCondition.temp)
                        }
                        _dataFetchState.value = true
                        _weather.value = weather

                        repository.deleteWeatherData()
                        repository.storeWeatherData(weather)
                    } else {
                        _weather.postValue(null)
                        _dataFetchState.postValue(false)
                    }
                }
                is Error -> {
                    _isLoading.value = false
                    _dataFetchState.value = false
                }
                is Loading -> _isLoading.postValue(true)
            }
        }
    }

     fun saveLocation(location:LocationModel){
        viewModelScope.launch {
            repository.saveLocation(location)
        }
    }

    fun saveCityId(cityId : Int){
        viewModelScope.launch {
            repository.saveCityId(cityId)
        }
    }

   fun getSelectedTemperatureUnit() : String? {
        return repository.getSelectedTemperatureUnit()
   }

    override fun onCleared() {

        super.onCleared()
    }

}
