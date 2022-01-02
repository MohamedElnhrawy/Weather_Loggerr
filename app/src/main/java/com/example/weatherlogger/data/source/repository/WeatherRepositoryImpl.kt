package com.example.weatherlogger.data.source.repository

import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.data.model.WeatherForecast
import com.example.weatherlogger.data.source.local.WeatherLocalDataSource
import com.example.weatherlogger.data.source.local.preference.AppPreferences
import com.example.weatherlogger.data.source.remote.WeatherRemoteDataSource
import com.example.weatherlogger.di.scope.IoDispatcher
import com.example.weatherlogger.mapper.WeatherForecastMapperLocal
import com.example.weatherlogger.mapper.WeatherForecastMapperRemote
import com.example.weatherlogger.mapper.WeatherMapperLocal
import com.example.weatherlogger.mapper.WeatherMapperRemote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.weatherlogger.utils.Result
import java.util.*

class WeatherRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WeatherRepository {

    override suspend fun getWeather(location: LocationModel, refresh: Boolean): Result<Weather> =
        withContext(ioDispatcher) {
            if (refresh) {
                val mapper = WeatherMapperRemote()
                when (val response = remoteDataSource.getWeather(location)) {
                    is Result.Success -> {
                        if (response.data != null) {
                            response.data.createdTime = Calendar.getInstance().time.toString()
                            Result.Success(mapper.transformToDomain(response.data))
                        } else {
                            Result.Success(null)
                        }
                    }

                    is Result.Error -> {
                        Result.Error(response.exception)
                    }

                    else -> Result.Loading
                }
            } else {
                val mapper = WeatherMapperLocal()
                val forecast = localDataSource.getWeather()
                if (forecast != null) {
                    Result.Success(mapper.transformToDomain(forecast))
                } else {
                    Result.Success(null)
                }
            }
        }

    override suspend fun getForecastWeather(
        cityId: Int,
        refresh: Boolean
    ): Result<List<WeatherForecast>?> = withContext(ioDispatcher) {
        if (refresh) {
            val mapper = WeatherForecastMapperRemote()
            when (val response = remoteDataSource.getWeatherForecast(cityId)) {
                is Result.Success -> {
                    if (response.data != null) {
                        Result.Success(mapper.transformToDomain(response.data))
                    } else {
                        Result.Success(null)
                    }
                }

                is Result.Error -> {
                    Result.Error(response.exception)
                }

                else -> Result.Loading
            }
        } else {
            val mapper = WeatherForecastMapperLocal()
            val forecast = localDataSource.getForecastWeather()
            if (forecast != null) {
                Result.Success(mapper.transformToDomain(forecast))
            } else {
                Result.Success(null)
            }
        }
    }

    override suspend fun storeWeatherData(weather: Weather) = withContext(ioDispatcher) {
        val mapper = WeatherMapperLocal()
        localDataSource.saveWeather(mapper.transformToDto(weather))
    }

    override suspend fun storeForecastData(forecasts: List<WeatherForecast>) =
        withContext(ioDispatcher) {
            val mapper = WeatherForecastMapperLocal()
            mapper.transformToDto(forecasts).let { listOfDbForecast ->
                listOfDbForecast.forEach {
                    localDataSource.saveForecastWeather(it)
                }
            }
        }

    override suspend fun getSearchWeather(location: String): Result<List<SearchResult>?> =
        withContext(ioDispatcher) {
            return@withContext when (val response = remoteDataSource.getSearchWeather(location)) {
                is Result.Success -> {
                    if (response.data != null) {
                        Result.Success(response.data)
                    } else {
                        Result.Success(null)
                    }
                }
                is Result.Error -> {
                    Result.Error(response.exception)
                }
                else -> {
                    Result.Loading
                }
            }
        }

    override suspend fun deleteWeatherData() = withContext(ioDispatcher) {
        localDataSource.deleteWeather()
    }

    override suspend fun deleteForecastData() {
        localDataSource.deleteForecastWeather()
    }

    override suspend fun saveLocation(location: LocationModel) {
        preferences.saveLocation(location)
    }

    override suspend fun saveCityId(id: Int) {
        preferences.saveCityId(id)
    }

    override suspend fun getCityId(): Int {
        return preferences.getCityId()
    }

    override  fun getSelectedTemperatureUnit(): String? {
        return preferences.getSelectedTemperatureUnit()
    }
}