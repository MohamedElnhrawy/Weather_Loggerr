package com.example.weatherlogger

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import com.example.weatherlogger.data.source.local.preference.AppPreferences
import com.example.weatherlogger.data.source.repository.WeatherRepository
import com.example.weatherlogger.worker.CustomWorkerFactory
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class WeatherLoggerApplication : Application() , Configuration.Provider{ //,

    @Inject
    lateinit var weatherRepository: WeatherRepository
    @Inject
    lateinit var appPreferences: AppPreferences


    override fun getWorkManagerConfiguration(): Configuration {
        val myWorkerFactory = DelegatingWorkerFactory()
        myWorkerFactory.addFactory(CustomWorkerFactory(weatherRepository,appPreferences))
        // Add here other factories that you may need in this application

        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(myWorkerFactory)
            .build()
    }


    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
    }


}