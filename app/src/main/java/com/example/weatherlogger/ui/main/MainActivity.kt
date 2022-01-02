package com.example.weatherlogger.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherlogger.R
import com.example.weatherlogger.databinding.ActivityMainBinding
import com.example.weatherlogger.ui.base.BaseActivity
import javax.inject.Inject
import dagger.hilt.android.AndroidEntryPoint
import androidx.databinding.library.baseAdapters.BR
import androidx.navigation.ui.setupWithNavController


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    @Inject
    lateinit var mMainViewModel: MainViewModel
    private lateinit var mActivityMainBinding: ActivityMainBinding
    private lateinit var mContext: Context
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun getViewModel(): MainViewModel = mMainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mActivityMainBinding = getViewDataBinding()
        setupFragment()

    }

    private fun setupFragment() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        mActivityMainBinding.bottomNavigationView.setupWithNavController(navController)
    }

    fun setTitle(title: String, isBackButton: Boolean = false) {
        mActivityMainBinding.tvTitle.text = title
        if (isBackButton) {
            setBackButton(isBackButton)
        } else {
            mActivityMainBinding.icBack.visibility = View.GONE
        }
    }

    fun setBackButton(show: Boolean) {
        if (show)
            mActivityMainBinding.icBack.visibility = View.VISIBLE
        else
            mActivityMainBinding.icBack.visibility = View.GONE

        mActivityMainBinding.icBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun setEmptyView(isEmpty: Boolean = false) {
        if (isEmpty) {
            mActivityMainBinding.emptyList.visibility = View.VISIBLE
        } else {
            mActivityMainBinding.emptyList.visibility = View.GONE
        }
    }

    fun setupSaveIcon(show: Boolean) {
        if (show)
            mActivityMainBinding.icSave.visibility = View.VISIBLE
        else
            mActivityMainBinding.icSave.visibility = View.GONE

        mActivityMainBinding.icSave.setOnClickListener {

        }
    }


}