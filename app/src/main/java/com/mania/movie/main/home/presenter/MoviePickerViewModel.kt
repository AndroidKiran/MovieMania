package com.mania.movie.main.home.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.SharedPreferences
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.mania.movie.MovieManiaApplication
import com.mania.movie.di.scope.PerActivity
import com.mania.movie.helper.PreferenceHelper
import com.mania.movie.helper.Utils
import com.mania.movie.main.base.BaseViewModel
import com.mania.movie.main.home.repository.MovieApiRepository
import com.mania.movie.main.home.repository.MovieDbRepository
import com.mania.movie.main.home.repository.model.MoviePickerListModel
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.mvvm.switchMap
import com.mania.movie.rx.getObservableAsync
import com.mania.movie.rx.toLiveData
import io.reactivex.Observable
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class MoviePickerViewModel @Inject constructor(application: MovieManiaApplication,
                                               private val apiRepository: MovieApiRepository,
                                               private val dbRepository: MovieDbRepository,
                                               private val preferenceHelper: PreferenceHelper) : BaseViewModel(application) {

    var queryLiveData = MutableLiveData<String>()

    var movieListLiveData: LiveData<MoviePickerListModel> = queryLiveData.switchMap {
        when (it) {
            null -> MutableLiveData()
            else -> apiRepository.getMoviesForQuery(it)
                    .debounce(1000,TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .toLiveData()

        }
    }

    var successLiveData = MutableLiveData<Boolean>()

    var userPic = ObservableField<String>(Utils.getPhotoUrl(preferenceHelper))

    var isLoggedIn = MutableLiveData<Boolean>()

    init {
        isLoggedIn.postValue(Utils.isLoggedIn(preferenceHelper))
    }


    fun insertBookmarkMovie(moviePickerModel: MoviePickerModel) =
            getCompositeDisposable().add(
                    dbRepository.insertMovie(moviePickerModel.apply {
                        userId = Utils.getUserID(preferenceHelper)!!
                    })
                            .subscribe({
                                handleOnSuccess()
                            },{
                                handleOnError(it)
                            })
            )


    fun getPreferenceHelper() = preferenceHelper

    private fun handleOnSuccess() {
        successLiveData.postValue(true)
    }

    private fun handleOnError(throwable: Throwable) {
        errorLiveData.postValue(true)
    }

}