package com.mania.movie.main.bookmark.ui

import com.mania.movie.main.home.repository.model.MoviePickerModel

interface IBookNavigator {

    fun onMoviePick(moviePickerModel: MoviePickerModel)

}