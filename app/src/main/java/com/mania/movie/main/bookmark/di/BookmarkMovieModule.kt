package com.mania.movie.main.bookmark.di

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.mania.movie.di.qualifier.FragmentContext
import com.mania.movie.di.scope.PerFragment
import com.mania.movie.main.bookmark.ui.BookmarkAdapter
import com.mania.movie.main.bookmark.ui.BookmarkMoviesFragment
import com.mania.movie.room.AppDatabase
import com.mania.movie.room.IMoviePickerDao
import dagger.Module
import dagger.Provides

@Module
class BookmarkMovieModule {

    @Provides
    @PerFragment
    @FragmentContext
    fun provideFragmentContext(bookmarkMoviesFragment: BookmarkMoviesFragment): Context? = bookmarkMoviesFragment.context

    @Provides
    @PerFragment
    fun provideMoviePickerDao(appDatabase: AppDatabase): IMoviePickerDao = appDatabase.provideMoviePickerDao()

    @Provides
    @PerFragment
    fun provideBookmarkAdapter() = BookmarkAdapter()

    @Provides
    @PerFragment
    fun provideLinearLayoutManager(@FragmentContext context: Context?) = LinearLayoutManager(context)
}