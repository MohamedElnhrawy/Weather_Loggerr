package com.example.weatherlogger.data.source.local

import com.example.weatherlogger.data.source.local.dao.WeatherDao
import com.example.weatherlogger.data.source.local.entity.DBWeather
import com.example.weatherlogger.data.source.local.entity.DBWeatherForecast
import com.example.weatherlogger.di.scope.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WeatherLocalDataSource {
    override suspend fun getWeather(): DBWeather = withContext(ioDispatcher) {
        return@withContext weatherDao.getWeather()
    }

    override suspend fun saveWeather(weather: DBWeather) = withContext(ioDispatcher) {
        weatherDao.insertWeather(weather)
    }

    override suspend fun deleteWeather() = withContext(ioDispatcher) {
        weatherDao.deleteAllWeather()
    }

    override suspend fun getForecastWeather(): List<DBWeatherForecast>? =
        withContext(ioDispatcher) {
            return@withContext weatherDao.getAllWeatherForecast()
        }

    override suspend fun saveForecastWeather(weatherForecast: DBWeatherForecast) =
        withContext(ioDispatcher) {
            weatherDao.insertForecastWeather(weatherForecast)
        }

    override suspend fun deleteForecastWeather() = withContext(ioDispatcher) {
        weatherDao.deleteAllWeatherForecast()
    }
}
