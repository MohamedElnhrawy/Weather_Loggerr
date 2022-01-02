package com.example.weatherlogger.data.source.remote.retrofit

import com.example.weatherlogger.data.model.NetworkWeather
import com.example.weatherlogger.data.model.NetworkWeatherForecastResponse
import com.example.weatherlogger.data.model.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    /**
     * This function gets the [NetworkWeather] for the [location] the
     * user searched for.
     */
    @GET("/geo/1.0/direct")
    suspend fun getSpecificWeather(
        @Query("q") location: String
    ): Response<List<SearchResult>>

    // This function gets the weather information for the user's location.
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<NetworkWeather>

    // This function gets the weather forecast information for the user's location.
    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("id") cityId: Int
    ): Response<NetworkWeatherForecastResponse>
}
