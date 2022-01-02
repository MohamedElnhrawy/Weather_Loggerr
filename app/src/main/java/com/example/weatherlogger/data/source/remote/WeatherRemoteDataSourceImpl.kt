package com.example.weatherlogger.data.source.remote

import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.data.model.NetworkWeather
import com.example.weatherlogger.data.model.NetworkWeatherForecast
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.data.source.remote.retrofit.WeatherApiService
import com.example.weatherlogger.di.scope.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.weatherlogger.utils.Result


class WeatherRemoteDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: WeatherApiService
) : WeatherRemoteDataSource {
    override suspend fun getWeather(location: LocationModel): Result<NetworkWeather> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getCurrentWeather(
                    location.latitude, location.longitude
                )
                if (result.isSuccessful) {
                    val networkWeather = result.body()
                    Result.Success(networkWeather)
                } else {
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getWeatherForecast(cityId: Int): Result<List<NetworkWeatherForecast>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getWeatherForecast(cityId)
                if (result.isSuccessful) {
                    val networkWeatherForecast = result.body()?.weathers
                    Result.Success(networkWeatherForecast)
                } else {
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getSearchWeather(query: String): Result<List<SearchResult>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getSpecificWeather(query)
                if (result.isSuccessful) {
                    val networkWeather = result.body()
                    Result.Success(networkWeather)
                } else {
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

}