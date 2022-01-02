package com.example.weatherlogger.ui.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherlogger.databinding.FragmentSearchBinding
import com.example.weatherlogger.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherlogger.R
import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.ui.main.MainActivity
import com.example.weatherlogger.utils.BaseBottomSheetDialog
import com.example.weatherlogger.utils.convertCelsiusToFahrenheit
import com.example.weatherlogger.utils.convertKelvinToCelsius
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(),
    ResultItemViewModel.ResultItemViewModelListener {

    private val searchResultAdapter by lazy { ResultAdapter(ArrayList(),this) }

    @Inject
    lateinit var homeViewModel: SearchViewModel

    private lateinit var fragmentHomeBinding: FragmentSearchBinding

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_search

    override fun getViewModel(): SearchViewModel {
        return homeViewModel
    }

    override fun getLifeCycleOwner(): LifecycleOwner = this


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = getViewDataBinding()
        setUp()
    }
    private fun setUp() {
        (activity as MainActivity).setBackButton(true)
        val recyclerView = fragmentHomeBinding.locationSearchRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = searchResultAdapter

        fragmentHomeBinding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    homeViewModel.getSearchWeather(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        with(homeViewModel) {
            locations.observe(viewLifecycleOwner) { hits ->
                fragmentHomeBinding.zeroHits.isVisible = hits.isEmpty()
            }

            weatherInfo.observe(viewLifecycleOwner) { list ->
                list?.let {
                    searchResultAdapter.updateList(it)
                }
            }

            isLoading.observe(viewLifecycleOwner) { state ->
                fragmentHomeBinding.searchWeatherLoader.isVisible = state
            }

            dataFetchState.observe(viewLifecycleOwner) { state ->
                if (!state) {
                    showLongSnackBar("An error occurred! Please try again.")
                }
            }

        }

    }

 override fun onItemClick(result: SearchResult) {
        viewDetails(result)
    }

    fun viewDetails(result: SearchResult){
        var bundle = bundleOf("result" to result)
        NavHostFragment.findNavController(this).navigate(
            R.id.action_navigation_search_to_detailsFragment,
            bundle
        )
    }
}
