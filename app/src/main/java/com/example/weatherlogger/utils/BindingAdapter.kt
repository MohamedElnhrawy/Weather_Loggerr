package com.example.weatherlogger.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.R
import com.example.weatherlogger.data.source.local.preference.AppPreferences
import com.github.pwittchen.weathericonview.WeatherIconView


@BindingAdapter("setIcon")
fun WeatherIconView.showIcon(condition: String?) {
    val context = this.context
    WeatherIconGenerator.getIconResources(context, this, condition)
}

@BindingAdapter("setTemperature")
fun TextView.setTemperature(double: Double) {
    val context = this.context
    if (AppPreferences(context).getSelectedTemperatureUnit() == context.getString(
        R.string.temp_unit_fahrenheit
    )
    )
        this.text = double.toString() + context.resources.getString(R.string.temp_symbol_fahrenheit)
    else
        this.text = double.toString() + context.resources.getString(R.string.temp_symbol_celsius)


    @BindingAdapter("visibleIf")
    fun View.visibleIf(visible:Boolean) {
      if (visible){
          this.visibility = View.VISIBLE
      }else{
          this.visibility = View.GONE
      }
    }

}
@BindingAdapter("setFormattedDate")
fun TextView.setFormattedDate(date: String?) {
    date?.let {
        this.text = it.formatDate()
    }

}

