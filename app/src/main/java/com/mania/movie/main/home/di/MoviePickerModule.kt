package com.mania.movie.main.home.di

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.mania.movie.di.qualifier.ActivityContext
import com.mania.movie.di.scope.PerActivity
import com.mania.movie.di.scope.PerApplication
import com.mania.movie.helper.ItemOffsetDecoration
import com.mania.movie.helper.PreferenceHelper
import com.mania.movie.helper.Utils
import com.mania.movie.main.home.repository.IMovieApi
import com.mania.movie.main.home.ui.MoviePickerActivity
import com.mania.movie.main.home.ui.MoviePickerAdapter
import com.mania.movie.room.AppDatabase
import com.mania.movie.room.IMoviePickerDao
import com.mania.movie.room.IReviewDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MoviePickerModule {

    @Provides
    @PerActivity
    @ActivityContext
    fun provideActivityContext(moviePickerActivity: MoviePickerActivity): Context = moviePickerActivity

    @Provides
    @PerActivity
    fun provideMovieApi(retrofit: Retrofit): IMovieApi = retrofit.create(IMovieApi::class.java)

    @Provides
    @PerActivity
    fun provideMoviePickerAdapter(preferenceHelper: PreferenceHelper) = MoviePickerAdapter(preferenceHelper)

    @Provides
    @PerActivity
    fun provideGridLayoutManager(@ActivityContext context: Context?) = GridLayoutManager(context, 2)

    @Provides
    @PerActivity
    fun provideItemDecoration() = ItemOffsetDecoration(12)

    @Provides
    @PerActivity
    fun provideMoviePickerDao(appDatabase: AppDatabase): IMoviePickerDao = appDatabase.provideMoviePickerDao()

    @Provides
    @PerActivity
    fun provideReviewDao(appDatabase: AppDatabase): IReviewDao = appDatabase.provideReviewPickerDao()
}