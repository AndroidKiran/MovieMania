package com.mania.movie.di.module

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import com.mania.movie.MovieManiaApplication
import com.mania.movie.di.scope.PerActivity
import com.mania.movie.di.scope.PerApplication
import com.mania.movie.room.AppDatabase
import com.mania.movie.room.IMoviePickerDao
import dagger.Module
import dagger.Provides

@Module
class RoomModule constructor(private val application: MovieManiaApplication) {


    @Provides
    @PerApplication
    fun provideAppDb(): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DB_NAME)
                    .build()
}