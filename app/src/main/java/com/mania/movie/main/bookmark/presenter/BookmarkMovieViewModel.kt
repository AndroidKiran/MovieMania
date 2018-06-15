package com.mania.movie.main.bookmark.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mania.movie.MovieManiaApplication
import com.mania.movie.di.scope.PerActivity
import com.mania.movie.main.base.BaseViewModel
import com.mania.movie.main.home.repository.MovieDbRepository
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.mvvm.switchMap
import javax.inject.Inject

@PerActivity
class BookmarkMovieViewModel @Inject constructor(application: MovieManiaApplication,
                                                 private val dbRepository: MovieDbRepository) : BaseViewModel(application) {


    var retryGetBookmarkMovies = MutableLiveData<Boolean>()

    var bookmarkMovieListLiveData: LiveData<List<MoviePickerModel>> = retryGetBookmarkMovies.switchMap {
        when (it) {
            null -> MutableLiveData()
            else -> dbRepository.getBookMarkMovies()
        }
    }

    init {
        retryGetBookmarkMovies.postValue(true)
    }

}