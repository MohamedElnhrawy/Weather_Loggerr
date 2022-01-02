package com.example.weatherlogger.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherlogger.data.source.local.dao.WeatherDao
import com.example.weatherlogger.data.source.local.entity.DBWeather
import com.example.weatherlogger.data.source.local.entity.DBWeatherForecast
import com.example.weatherlogger.utils.typeconverters.ListNetworkWeatherDescriptionConverter


@Database(entities = [DBWeather::class, DBWeatherForecast::class], version = 1, exportSchema = false)
@TypeConverters(
    ListNetworkWeatherDescriptionConverter::class
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val weatherDao: WeatherDao
}
