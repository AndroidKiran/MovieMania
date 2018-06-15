package com.mania.movie.main.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mania.movie.binding.SnackbarConfiguration
import com.mania.movie.mvvm.observe
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<out B : ViewDataBinding, out V : BaseViewModel> : Fragment() {

    val snackBarConfiguration = SnackbarConfiguration()
    var fragmentTransactionListener: IFragmentCallback? = null

    private lateinit var baseViewDataBinding: B
    private lateinit var baseViewModel: V
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        baseViewModel = getViewModel()
        baseViewModel.onViewCreated()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            DataBindingUtil.inflate<B>(inflater, getLayoutId(), container, false).also {
                baseViewDataBinding = it
                rootView = baseViewDataBinding.root
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executePendingVariablesBinding()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        performDependencyInjection()
        fragmentTransactionListener = context as IFragmentCallback
    }

    override fun onDestroyView() {
        baseViewModel.onDestroyView()
        super.onDestroyView()
    }

    override fun onDetach() {
        fragmentTransactionListener = null
        super.onDetach()
    }

    fun getViewDataBinding(): B = baseViewDataBinding

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    abstract fun getViewModel(): V

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun executePendingVariablesBinding()
}