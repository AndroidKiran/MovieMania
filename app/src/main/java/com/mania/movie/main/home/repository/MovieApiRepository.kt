package com.mania.movie.main.home.repository

import android.arch.lifecycle.LiveData
import android.support.v4.util.ArrayMap
import com.mania.movie.BuildConfig
import com.mania.movie.helper.Result
import com.mania.movie.main.details.repository.model.MovieDetailModel
import com.mania.movie.main.home.repository.model.MoviePickerListModel
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.rx.*
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class MovieApiRepository @Inject constructor(private val movieApi: IMovieApi, private val schedulerProvider: SchedulerProvider){

    companion object {
        private const val USER_KEY = "apikey"
    }

    fun getMoviesForQuery(name: String): Flowable<Result<MoviePickerListModel>> {

        val data = ArrayMap<String, String>().apply {
            put("s", name)
            put(USER_KEY, BuildConfig.API_KEY)
        }

        return movieApi.searchMovies(options = data)
                .getSingleAsync(schedulerProvider)
                .toFlowable()
                .apibaseResponseToResult()
    }

    fun getMovieDetailById(movieId: String): Single<MovieDetailModel> {
        val data = ArrayMap<String, String>().apply {
            put("i", movieId)
            put(USER_KEY, BuildConfig.API_KEY)
        }

        return movieApi.getMovieDetailById(options = data)
    }

    fun getSchedulerProvider() = schedulerProvider
}