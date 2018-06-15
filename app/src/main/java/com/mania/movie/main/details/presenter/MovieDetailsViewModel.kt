package com.mania.movie.main.details.presenter

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import com.mania.movie.MovieManiaApplication
import com.mania.movie.main.base.BaseViewModel
import com.mania.movie.main.home.repository.MovieApiRepository
import com.mania.movie.mvvm.switchMap
import com.mania.movie.rx.getFlowableAsync
import com.mania.movie.rx.toLiveData
import io.reactivex.Flowable
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(application: MovieManiaApplication, private val apiRepository: MovieApiRepository): BaseViewModel(application) {

    var queryLiveData = MutableLiveData<String>()

    var loadingBinding = ObservableBoolean()

    var movieDetailLiveData = queryLiveData.switchMap {
        when (it) {
            null -> MutableLiveData()
            else -> loadMovies(it)
        }
    }

    private fun loadMovies(name: String) =
            apiRepository.getMovieDetailById(name)
                    .toFlowable()
                    .onErrorResumeNext(Flowable.empty())
                    .doOnSubscribe { loadingBinding.set(true) }
                    .getFlowableAsync(apiRepository.getSchedulerProvider())
                    .toLiveData()
}