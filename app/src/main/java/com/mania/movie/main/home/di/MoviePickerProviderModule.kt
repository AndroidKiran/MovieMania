package com.mania.movie.main.home.di

import android.arch.lifecycle.ViewModel
import com.mania.movie.di.module.ViewModelKey
import com.mania.movie.di.scope.PerFragment
import com.mania.movie.main.bookmark.di.BookmarkMovieModule
import com.mania.movie.main.bookmark.presenter.BookmarkMovieViewModel
import com.mania.movie.main.bookmark.ui.BookmarkMoviesFragment
import com.mania.movie.main.details.presenter.MovieDetailsViewModel
import com.mania.movie.main.details.ui.MovieDetailFragment
import com.mania.movie.main.home.presenter.MoviePickerViewModel
import com.mania.movie.main.review.di.ReviewModule
import com.mania.movie.main.review.presenter.ReviewListViewModel
import com.mania.movie.main.review.presenter.ReviewViewModel
import com.mania.movie.main.review.ui.ReviewFragment
import com.mania.movie.main.review.ui.ReviewListFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MoviePickerProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(MoviePickerViewModel::class)
    abstract fun bindHomeViewModel(viewModel: MoviePickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookmarkMovieViewModel::class)
    abstract fun bindBookmarkViewModel(viewModel: BookmarkMovieViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector(modules = [(BookmarkMovieModule::class)])
    abstract fun bindBookmarkFragment(): BookmarkMoviesFragment

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    abstract fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindMovieDetailFragment(): MovieDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(ReviewViewModel::class)
    abstract fun bindReviewViewModel(viewModel: ReviewViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector
    abstract fun bindReviewFragment(): ReviewFragment

    @Binds
    @IntoMap
    @ViewModelKey(ReviewListViewModel::class)
    abstract fun bindReviewListViewModel(viewModel: ReviewListViewModel): ViewModel

    @PerFragment
    @ContributesAndroidInjector(modules = [(ReviewModule::class)])
    abstract fun bindReviewListFragment(): ReviewListFragment
}