package com.mania.movie.main.details.repository.model

import com.google.gson.annotations.SerializedName

data class MovieRating constructor(
        @SerializedName("Source")
        var source: String = "",
        @SerializedName("Value")
        var value: String = ""
)