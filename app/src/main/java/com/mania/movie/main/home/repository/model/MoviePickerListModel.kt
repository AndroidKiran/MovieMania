package com.mania.movie.main.home.repository.model

import com.google.gson.annotations.SerializedName

data class MoviePickerListModel constructor(@SerializedName("Search")
                                            var searchList: List<MoviePickerModel>? = null)