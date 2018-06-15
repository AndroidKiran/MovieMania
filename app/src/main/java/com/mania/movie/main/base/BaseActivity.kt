package com.mania.movie.main.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mania.movie.R
import com.mania.movie.binding.SnackbarConfiguration
import com.mania.movie.extension.showActionSnackBar
import com.mania.movie.extension.showSnackBar
import com.mania.movie.mvvm.NetworkLiveData
import com.mania.movie.mvvm.observe
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<out B : ViewDataBinding, out V : BaseViewModel> : AppCompatActivity(), IFragmentCallback {

    @Inject
    lateinit var networkLiveData: NetworkLiveData

    val snackBarConfiguration = SnackbarConfiguration()

    private lateinit var baseViewDataBinding: B
    private lateinit var baseViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
        observerApiCallErrorChange()
        attachNetworkReceiver()
    }

    private fun performDataBinding() {
        baseViewDataBinding = DataBindingUtil.setContentView(this@BaseActivity, getLayoutId())
        baseViewModel = getViewModel()
        executePendingVariablesBinding()
        baseViewModel.onViewCreated()
        baseViewDataBinding.executePendingBindings()
    }

    override fun onDestroy() {
        baseViewModel.onDestroyView()
        super.onDestroy()
    }

    override fun onFragmentInteraction(visible: Int) {
    }

    fun hideKeyboard() =
            this.currentFocus?.let {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }


    abstract fun getViewModel(): V

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun executePendingVariablesBinding()

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    fun getViewDataBinding(): B = baseViewDataBinding


    private fun observerApiCallErrorChange() = baseViewModel.errorLiveData.observe(this@BaseActivity) {

        it?.let {
            if (it) {
                hideKeyboard()
                snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg), SnackbarConfiguration.Type.NEUTRAL, Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun attachNetworkReceiver() {
        var isSnackBarShown = false
        networkLiveData.observe(this) {
            it?.let {
                baseViewModel.isNetworkActive.set(it)

                if (it) {
                    if (isSnackBarShown) {
                        snackBarConfiguration.showSnackBar(getString(R.string.str_internet_title),
                                SnackbarConfiguration.Type.VALID, Snackbar.LENGTH_SHORT)
                        isSnackBarShown = false
                    }
                } else {
                    isSnackBarShown = true
                    snackBarConfiguration.showActionSnackBar(getString(R.string.str_no_internet_title),
                            getString(R.string.str_dismiss),
                            View.OnClickListener { }, Snackbar.LENGTH_INDEFINITE)
                }
            }
        }
    }

}
