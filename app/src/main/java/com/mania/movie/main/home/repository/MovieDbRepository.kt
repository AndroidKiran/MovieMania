package com.mania.movie.main.home.repository

import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.room.IMoviePickerDao
import com.mania.movie.rx.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

class MovieDbRepository @Inject constructor(private val moviePickerDao: IMoviePickerDao, private val schedulerProvider: SchedulerProvider) {

    fun insertMovie(moviePickerModel: MoviePickerModel) =
            Completable.fromCallable {
                moviePickerDao.insert(moviePickerModel)
            }.getCompletableAsync(schedulerProvider)

    fun getBookMarkMovies(userID: String) =
            moviePickerDao.getBookmarks(userID)
                    .toFlowable()
                    .getFlowableAsync(schedulerProvider)
                    .onErrorResumeNext(Flowable.empty())
                    .toLiveData()

}