package com.mania.movie.main.home.ui

import com.mania.movie.main.home.repository.model.MoviePickerModel

interface IMoviePickerNavigator {

    fun onMoviePick(moviePickerModel: MoviePickerModel)

    fun onMovieLike(moviePickerModel: MoviePickerModel)

    fun onMovieBookmark(moviePickerModel: MoviePickerModel)

    fun onMovieReview(moviePickerModel: MoviePickerModel)

}