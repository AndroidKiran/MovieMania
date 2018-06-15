package com.mania.movie.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.main.review.repository.ReviewModel
import com.mania.movie.room.AppDatabase.Companion.DB_VERSION

@Database(entities = [(MoviePickerModel::class),(ReviewModel::class)], version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "movie_mania.db"
        const val DB_VERSION = 2
    }

    abstract fun provideMoviePickerDao(): IMoviePickerDao

    abstract fun provideReviewPickerDao(): IReviewDao

}
