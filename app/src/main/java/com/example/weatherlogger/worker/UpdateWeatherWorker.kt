package com.example.weatherlogger.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.weatherlogger.data.source.local.preference.AppPreferences
import com.example.weatherlogger.data.source.repository.WeatherRepository
import com.example.weatherlogger.utils.NotificationHelper
import com.example.weatherlogger.utils.Result.Success

class  UpdateWeatherWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: WeatherRepository,
    private val preferences: AppPreferences,

    ) : CoroutineWorker(context, params) {
    private val notificationHelper = NotificationHelper("Weather Update", context)

    override suspend fun doWork(): Result {
        return when (val result = repository.getWeather(preferences.getLocation(), true)) {
            is Success -> {
                if (result.data != null) {
                    when (
                        val foreResult =
                            repository.getForecastWeather(result.data.cityId, true)
                    ) {
                        is Success -> {
                            if (foreResult.data != null) {
                                notificationHelper.createNotification()
                                Result.success()
                            } else {
                                Result.failure()
                            }
                        }
                        else -> Result.failure()
                    }
                } else {
                    Result.failure()
                }
            }
            else -> Result.failure()
        }
    }
}