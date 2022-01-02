package com.example.weatherlogger.ui.search

import androidx.lifecycle.MutableLiveData
import com.example.weatherlogger.data.model.SearchResult


class ResultItemViewModel(var result: SearchResult,var mListener: ResultItemViewModelListener) {

    var searchResult = MutableLiveData<SearchResult>()

    init {
        searchResult.value = result
    }

    fun onItemClick() {
        mListener.onItemClick(result)
    }
    interface ResultItemViewModelListener {
        fun onItemClick(result: SearchResult)
    }
}