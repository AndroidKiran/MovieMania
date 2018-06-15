package com.mania.movie.main.details.repository.model

import com.google.gson.annotations.SerializedName

data class MovieDetailModel constructor(
        @SerializedName("Title")
        var name: String = "",
        @SerializedName("Year")
        var year: String = "",
        @SerializedName("Genre")
        var genre: String = "",
        @SerializedName("Director")
        var director: String = "",
        @SerializedName("Plot")
        var plot: String = "",
        @SerializedName("Language")
        var language: String = "",
        @SerializedName("Country")
        var country: String = "",
        @SerializedName("Poster")
        var poster: String = "",
        @SerializedName("imdbRating")
        var imdbRating: String = "",
        @SerializedName("imdbVotes")
        var imdbVotes: String = "",
        @SerializedName("Ratings")
        var ratings: List<MovieRating>? = null
)