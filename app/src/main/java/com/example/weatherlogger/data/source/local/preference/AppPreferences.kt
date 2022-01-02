package com.example.weatherlogger.data.source.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.utils.AppConstant
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(mContext: Context) {
    private val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
    private val PREF_KEY_LOCATION = "LOCATION"
    private val WEATHER_PREF_TIME = "Weather pref time"
    private val WEATHER_FORECAST_PREF_TIME = "Forecast pref time"
    private val TEMPERATURE_UNIT = "Temperature Unit"
    private val CITY_ID = "CITY_ID"

    private var mPrefs: SharedPreferences =
        mContext.getSharedPreferences(AppConstant.PREF_NAME, Context.MODE_PRIVATE)


    // Token ....
    fun getAccessToken(): String {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null).toString()
    }

    fun setAccessToken(accessToken: String) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply()
    }


    // Location .......
    fun saveLocation(location: LocationModel) {
        mPrefs.edit(commit = true) {
            val gson = Gson()
            val json = gson.toJson(location)
            putString(PREF_KEY_LOCATION, json)
        }
    }

    fun getLocation(): LocationModel {
        val gson = Gson()
        val json = mPrefs.getString(PREF_KEY_LOCATION, null)
        return gson.fromJson(json, LocationModel::class.java)
    }

    // Timing .........................
    fun saveTimeOfInitialWeatherFetch(time: Long) {
        mPrefs.edit(commit = true) {
            putLong(WEATHER_PREF_TIME, time)
        }
    }

    fun getTimeOfInitialWeatherFetch() = mPrefs.getLong(WEATHER_PREF_TIME, 0L)
    fun saveTimeOfInitialWeatherForecastFetch(time: Long) {
        mPrefs.edit(commit = true) {
            putLong(WEATHER_FORECAST_PREF_TIME, time)
        }
    }

    fun getTimeOfInitialWeatherForecastFetch() = mPrefs.getLong(WEATHER_FORECAST_PREF_TIME, 0L)
    fun getSelectedTemperatureUnit() = mPrefs.getString(TEMPERATURE_UNIT, "")


    fun saveCityId(cityId: Int) {
        mPrefs?.edit(commit = true) {
            putInt(CITY_ID, cityId)
        }
    }


    fun getCityId() = mPrefs?.getInt(CITY_ID, 0)


}