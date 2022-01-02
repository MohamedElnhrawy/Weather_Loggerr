package com.example.weatherlogger.ui.home

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.example.weatherlogger.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.work.*
import com.example.weatherlogger.R
import com.example.weatherlogger.data.model.Weather
import com.example.weatherlogger.databinding.FragmentHomeBinding
import com.example.weatherlogger.ui.main.MainActivity
import com.example.weatherlogger.utils.AppConstant.Companion.GPS_REQUEST_CHECK_SETTINGS
import com.example.weatherlogger.utils.GpsUtil
import com.example.weatherlogger.utils.convertCelsiusToFahrenheit
import com.example.weatherlogger.utils.observeOnce
import com.example.weatherlogger.worker.UpdateWeatherWorker
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit
import androidx.databinding.library.baseAdapters.BR



@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private var isGPSEnabled = false

    lateinit var currentWeatherDat:Weather

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun getViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun getLifeCycleOwner(): LifecycleOwner = this


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = getViewDataBinding()
        setUp()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GpsUtil(requireActivity()).turnGPSOn(object : GpsUtil.OnGpsListener {
            override fun gpsStatus(isGPSEnabled: Boolean) {
                this@HomeFragment.isGPSEnabled = isGPSEnabled
            }
        })
    }

    private fun setUp() {
        hideAllViews(true)
        observeViewModels()
        fragmentHomeBinding.swipeRefreshId.setOnRefreshListener {
            fragmentHomeBinding.errorText.visibility = View.GONE
            fragmentHomeBinding.progressBar.visibility = View.VISIBLE
            hideViews()
            initiateRefresh()
            fragmentHomeBinding.swipeRefreshId.isRefreshing = false
        }

        (activity as MainActivity).getViewDataBinding().icSave.setOnClickListener{
            invokeLocationAction()
        }

        fragmentHomeBinding.tvMore.setOnClickListener{
            viewDetails()
        }
    }




    private fun invokeLocationAction() {
        when {
            allPermissionsGranted() -> {
                homeViewModel.fetchLocationLiveData().observeOnce(
                    viewLifecycleOwner,
                    { location ->
                        if (location != null) {
                            homeViewModel.getWeather(location)
                            setupWorkManager()
                        }
                    }
                )
            }

            shouldShowRequestPermissionRationale() -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.location_permission))
                    .setMessage(getString(R.string.access_location_message))
                    .setNegativeButton(
                        getString(R.string.no)
                    ) { _, _ -> requireActivity().finish() }
                    .setPositiveButton(
                        getString(R.string.ask_me)
                    ) { _, _ ->
                        requestPermissions(REQUIRED_PERMISSIONS, LOCATION_REQUEST_CODE)
                    }
                    .show()
            }

            !isGPSEnabled -> {
                showShortSnackBar(getString(R.string.gps_required_message))
            }

            else -> {
                requestPermissions(REQUIRED_PERMISSIONS, LOCATION_REQUEST_CODE)
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestPermissionRationale() = REQUIRED_PERMISSIONS.all {
        shouldShowRequestPermissionRationale(it)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            invokeLocationAction()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        private const val LOCATION_REQUEST_CODE = 123
    }

    private fun setupWorkManager() {
        homeViewModel.fetchLocationLiveData().observeOnce(
            this,
            {
                homeViewModel.saveLocation(it)
            }
        )
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val weatherUpdateRequest =
            PeriodicWorkRequestBuilder<UpdateWeatherWorker>(6, TimeUnit.HOURS)
                .setConstraints(constraint)
                .setInitialDelay(6, TimeUnit.HOURS)
                .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "Update_weather_worker",
            ExistingPeriodicWorkPolicy.REPLACE, weatherUpdateRequest
        )
    }
    private fun observeViewModels() {
        with(homeViewModel) {
            weather.observe(viewLifecycleOwner) { weather ->
                weather?.let {
                    currentWeatherDat = it
                    homeViewModel.saveCityId(it.cityId)

                    if (homeViewModel.getSelectedTemperatureUnit() == activity?.resources?.getString(R.string.temp_unit_fahrenheit))
                        it.networkWeatherCondition.temp =
                            convertCelsiusToFahrenheit(it.networkWeatherCondition.temp)

//                    fragmentHomeBinding.weather = it
//                    fragmentHomeBinding.networkWeatherDescription = it.networkWeatherDescription.first()
                }
            }

            dataFetchState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    true -> {
                        unHideViews()
                        fragmentHomeBinding.errorText.visibility = View.GONE
                    }
                    false -> {
                        hideViews()
                        fragmentHomeBinding.apply {
                            errorText.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            loadingText.visibility = View.GONE
                        }
                    }
                }
            }

            isLoading.observe(viewLifecycleOwner) { state ->
                when (state) {
                    true -> {
                        hideViews()
                        fragmentHomeBinding.apply {
                            progressBar.visibility = View.VISIBLE
                            loadingText.visibility = View.VISIBLE
                            errorText.visibility = View.GONE
                        }
                    }
                    false -> {
                        fragmentHomeBinding.apply {
                            progressBar.visibility = View.GONE
                            loadingText.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun initiateRefresh() {
        homeViewModel.fetchLocationLiveData().observeOnce(
            viewLifecycleOwner,
            { location ->
                if (location != null) {
                    homeViewModel.refreshWeather(location)
                } else {
                    hideViews()
                    fragmentHomeBinding.apply {
                        errorText.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        loadingText.visibility = View.GONE
                    }
                }
            }
        )
    }

    private fun hideViews() {
        fragmentHomeBinding.apply {
            cardLocationInfo.visibility  = View.GONE
        }
    }

    private fun unHideViews() {
        fragmentHomeBinding.apply {
            cardLocationInfo.visibility  = View.VISIBLE
        }
    }

    private fun hideAllViews(state: Boolean) {
        if (state) {
            fragmentHomeBinding.apply {
                cardLocationInfo.visibility = View.GONE
                errorText.visibility = View.GONE
                progressBar.visibility = View.GONE
                loadingText.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        isGPSEnabled = true
                        invokeLocationAction()
                    }

                    Activity.RESULT_CANCELED -> {
                        Snackbar.make(
                            fragmentHomeBinding.root,
                            getString(R.string.enable_gps),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    fun viewDetails(){
        currentWeatherDat.let {
            var bundle = bundleOf("weather" to it,)
            findNavController(this).navigate(
                R.id.action_navigation_home_to_detailsFragment,
                bundle
            )
        }
    }

    override fun onResume() {
        (activity as MainActivity).setBackButton(false)

        super.onResume()
    }
}
