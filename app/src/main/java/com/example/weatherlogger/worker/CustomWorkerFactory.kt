package com.example.weatherlogger.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.weatherlogger.data.source.local.preference.AppPreferences
import com.example.weatherlogger.data.source.repository.WeatherRepository

class CustomWorkerFactory (private val repository: WeatherRepository,private val preferences: AppPreferences) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            UpdateWeatherWorker::class.java.name -> {
                UpdateWeatherWorker(appContext, workerParameters, repository,preferences)
            }

            else ->
                // Return null, so that the base class can delegate to the default WorkerFactory.
                null
        }
    }
}