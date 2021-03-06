package com.example.weatherlogger.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherlogger.data.model.NetworkWeatherCondition
import com.example.weatherlogger.data.model.NetworkWeatherDescription
import com.example.weatherlogger.data.model.Wind

@Entity(tableName = "weather_table")
data class DBWeather(

    @ColumnInfo(name = "unique_id")
    @PrimaryKey(autoGenerate = true)
    var uId: Int = 0,

    @ColumnInfo(name = "city_id")
    val cityId: Int,

    @ColumnInfo(name = "city_name")
    val cityName: String,

    @Embedded
    val wind: Wind,

    @ColumnInfo(name = "weather_details")
    val networkWeatherDescription: List<NetworkWeatherDescription>,

    @Embedded
    val networkWeatherCondition: NetworkWeatherCondition,

    @ColumnInfo(name = "created_time")
    val  createdTime : String
)
