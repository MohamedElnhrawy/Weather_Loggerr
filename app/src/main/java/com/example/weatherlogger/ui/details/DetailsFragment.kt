package com.example.weatherlogger.ui.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
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
import com.example.weatherlogger.R
import com.example.weatherlogger.data.model.LocationModel
import com.example.weatherlogger.data.model.SearchResult
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.databinding.FragmentDetailsBinding
import com.example.weatherlogger.ui.main.MainActivity
import com.example.weatherlogger.utils.convertCelsiusToFahrenheit


@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding, DetailsViewModel>() {

    @Inject
    lateinit var homeViewModel: DetailsViewModel

    lateinit var weather: Weather
    lateinit var searchResult: SearchResult

    private lateinit var fragmentHomeBinding: FragmentDetailsBinding

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_details

    override fun getViewModel(): DetailsViewModel {
        return homeViewModel
    }

    override fun getLifeCycleOwner(): LifecycleOwner = this


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = getViewDataBinding()
        observeViewModels()
        setUp()
    }


     fun setUp() {
        (activity as MainActivity).setBackButton(true)
        (activity as MainActivity).setupSaveIcon(false)
         if (requireArguments().containsKey("weather")){
             weather = arguments?.getParcelable<Weather>("weather")!!
             homeViewModel.currentWeather.value = weather
         }else if (requireArguments().containsKey("result")){
             searchResult = arguments?.getParcelable<SearchResult>("result")!!
             homeViewModel.refreshWeather(LocationModel(searchResult.lon,searchResult.lat))
         }
    }

    private fun observeViewModels() {
        with(homeViewModel) {
            weather.observe(viewLifecycleOwner) { weather ->
                weather?.let {
                    homeViewModel.currentWeather.value = weather
                    homeViewModel.saveCityId(it.cityId)

                }
            }


            isLoading.observe(viewLifecycleOwner) { state ->
                when (state) {
                    true -> {
                        fragmentHomeBinding.apply {
                            progressBar.visibility = View.VISIBLE
                            errorText.visibility = View.GONE
                        }
                    }
                    false -> {
                        fragmentHomeBinding.apply {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    override fun onDetach() {

        super.onDetach()
    }
}
