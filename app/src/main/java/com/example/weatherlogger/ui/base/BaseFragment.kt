package com.example.weatherlogger.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.weatherlogger.R
import com.example.weatherlogger.ui.details.DetailsViewModel
import com.example.weatherlogger.ui.home.HomeViewModel
import com.example.weatherlogger.ui.main.MainActivity
import com.example.weatherlogger.ui.search.SearchViewModel
import com.example.weatherlogger.utils.OnOneOffClickListener
import com.google.android.material.snackbar.Snackbar
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    var mActivity: BaseActivity<T, V>? = null
    lateinit var mRootView: View
    lateinit var mViewDataBinding: T
    lateinit var mViewModel: V
    abstract fun getBindingVariable(): Int
    abstract fun getLayoutId(): Int
    abstract fun getViewModel(): V
    abstract fun getLifeCycleOwner(): LifecycleOwner
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context
            mActivity = activity as BaseActivity<T, V>?
            mActivity?.onFragmentAttached()
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
        setHasOptionsMenu(false)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mRootView = mViewDataBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(getBindingVariable(), getViewModel())
        mViewDataBinding.lifecycleOwner = getLifeCycleOwner()
        mViewDataBinding.executePendingBindings()


        // setup title
        when (getViewModel()) {
            is HomeViewModel -> {
                (activity as MainActivity).setupSaveIcon(true)
            }
            is SearchViewModel -> {
                (activity as MainActivity).setupSaveIcon(false)
            }
            is DetailsViewModel -> {
                (activity as MainActivity).setupSaveIcon(false)
            }
            else -> {
                (activity as MainActivity).setTitle(getString(R.string.app_name))
            }
        }


        KeyboardVisibilityEvent.setEventListener(
            activity
        ) {
            if (it)
                mActivity?.let {
                    (it as MainActivity).getViewDataBinding().bottomNavigationView.visibility =
                        View.GONE
                }
            else
                mActivity?.let {
                    (it as MainActivity).getViewDataBinding().bottomNavigationView.visibility =
                        View.VISIBLE
                }

        }
    }


    fun setTitle(tile: String) {
        (activity as MainActivity).setTitle(tile, true)
    }

    fun setEmptyView(isEmpaty: Boolean = false) {
        (activity as MainActivity).setEmptyView(isEmpaty)
    }

    private fun performDependencyInjection() {
//        AndroidSupportInjection.inject(this)
    }

    fun getBaseActivity(): BaseActivity<T, V>? = mActivity

    fun getViewDataBinding(): T = mViewDataBinding

    fun showLoading() = mActivity?.showLoading()

    fun hideLoading() = mActivity?.hideLoading()

    fun hideKeyboard() = mActivity?.hideKeyboard()

    fun isNetworkConnected(): Boolean = mActivity != null && mActivity!!.isNetworkConnected()

    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

    fun showMessage(message: String) {
        mActivity?.showMessage(message)
    }

    fun onError(message: String?) {
        mActivity?.onError(message)
    }

    /**
     * prevent double click on view
     **/
    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        try {
            val safeClickListener = OnOneOffClickListener {
                onSafeClick(it)
            }
            setOnClickListener(safeClickListener)
        } catch (E: Exception) {
        }
    }

    fun showShortSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLongSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }
}
