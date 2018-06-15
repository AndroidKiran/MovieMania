package com.mania.movie.main.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import com.mania.movie.MovieManiaApplication
import com.mania.movie.mvvm.NetworkLiveData
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseViewModel constructor(application: MovieManiaApplication) : AndroidViewModel(application) {

    var isNetworkActive = ObservableBoolean()

    private lateinit var baseCompositeDisposable: CompositeDisposable

    var errorLiveData = MutableLiveData<Boolean>()

    fun onViewCreated() {
        baseCompositeDisposable = CompositeDisposable()
    }

    fun onDestroyView() {
        baseCompositeDisposable.dispose()
    }



    fun getCompositeDisposable(): CompositeDisposable = baseCompositeDisposable

}