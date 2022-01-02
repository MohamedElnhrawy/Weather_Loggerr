package com.example.weatherlogger.data.source.repository

import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.data.model.WeatherForecast
import com.example.weatherlogger.utils.Result


interface WeatherRepository {
    suspend fun getWeather(location: LocationModel, refresh: Boolean): Result<Weather?>

    suspend fun getForecastWeather(cityId: Int, refresh: Boolean): Result<List<WeatherForecast>?>

    suspend fun getSearchWeather(location: String): Result<List<SearchResult>?>

    suspend fun storeWeatherData(weather: Weather)

    suspend fun storeForecastData(forecasts: List<WeatherForecast>)

    suspend fun deleteWeatherData()

    suspend fun deleteForecastData()
    suspend fun saveLocation(location: LocationModel)
    suspend fun saveCityId(id: Int)
    suspend fun getCityId():Int
    fun getSelectedTemperatureUnit():String?

}