package com.example.weatherlogger.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SearchResult(
    val name: String,
    val country: String,
    val lat : Double,
    val lon : Double
): Parcelable
