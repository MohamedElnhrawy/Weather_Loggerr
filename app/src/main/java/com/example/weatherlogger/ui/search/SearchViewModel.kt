package com.example.weatherlogger.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.weatherlogger.BuildConfig
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.data.source.repository.WeatherRepository
import com.example.weatherlogger.ui.base.BaseViewModel
import com.example.weatherlogger.utils.Result
import com.example.weatherlogger.utils.asLiveData
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class SearchViewModel @Inject constructor(private val repository: WeatherRepository) : BaseViewModel(repository){

    private val _weatherInfo = MutableLiveData<List<SearchResult>?>()
    val weatherInfo = _weatherInfo.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading.asLiveData()

    private val _dataFetchState = MutableLiveData<Boolean>()
    val dataFetchState = _dataFetchState.asLiveData()
    val locations = MutableLiveData<List<SearchResult>>()


    fun getSearchWeather(name: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            when (val result = repository.getSearchWeather(name)) {
                is Result.Success -> {
                    _isLoading.value = false
                    if (result.data != null) {
                        Timber.i("Result ${result.data}")
                        _dataFetchState.value = true
                        _weatherInfo.postValue(result.data)
                    } else {
                        _weatherInfo.postValue(null)
                        _dataFetchState.postValue(false)
                    }
                }
                is Error -> {
                    _isLoading.value = false
                    _dataFetchState.value = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

    }

    companion object {
        const val HEADER_ITEM = 0
        const val LISTING_ITEM = 1
        const val AD_ITEM = 2
    }
}
