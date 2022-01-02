package com.example.weatherlogger.data.model


import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.util.*


data class NetworkWeather(

    val uId: Int,

    @SerializedName("id")
    val cityId: Int,

    val name: String,

    val wind: Wind,

    var createdTime: String = Calendar.getInstance().time.toString(),

    @SerializedName("weather")
    val networkWeatherDescriptions: List<NetworkWeatherDescription>,

    @SerializedName("main")
    val networkWeatherCondition: NetworkWeatherCondition
)
