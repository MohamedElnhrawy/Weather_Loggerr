package com.example.weatherlogger.data.source.remote

import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.data.model.NetworkWeather
import com.example.weatherlogger.data.model.NetworkWeatherForecast
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.utils.Result


interface WeatherRemoteDataSource {
    suspend fun getWeather(location: LocationModel): Result<NetworkWeather>

    suspend fun getWeatherForecast(cityId: Int): Result<List<NetworkWeatherForecast>>

    suspend fun getSearchWeather(query: String): Result<List<SearchResult>>
}