package com.mania.movie.main.home.repository

import android.support.v4.util.ArrayMap
import com.mania.movie.main.details.repository.model.MovieDetailModel
import com.mania.movie.main.home.repository.model.MoviePickerListModel
import com.mania.movie.main.home.repository.model.MoviePickerModel
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface IMovieApi {

    @GET("/")
    fun searchMovies(@QueryMap options: ArrayMap<String, String>): Single<MoviePickerListModel>

    @GET("/")
    fun getMovieDetailById(@QueryMap options: ArrayMap<String, String>): Single<MovieDetailModel>

//    @GET(value = "/genres/{ids}")
//    fun getGenres(@Path(value = "ids") ids: String,
//                  @QueryMap options: ArrayMap<String, String>): Single<List<OptionsModel>>
//
//    @GET(value = "/companies/{ids}")
//    fun getCompanies(@Path(value = "ids") ids: String,
//                     @QueryMap options: ArrayMap<String, String>): Single<List<OptionsModel>>
}