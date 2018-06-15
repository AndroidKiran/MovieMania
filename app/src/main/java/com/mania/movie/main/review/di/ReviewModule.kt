package com.mania.movie.main.review.di

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.mania.movie.di.qualifier.FragmentContext
import com.mania.movie.di.scope.PerFragment
import com.mania.movie.main.review.ui.ReviewAdapter
import com.mania.movie.main.review.ui.ReviewListFragment
import com.mania.movie.room.AppDatabase
import com.mania.movie.room.IReviewDao
import dagger.Module
import dagger.Provides

@Module
class ReviewModule {

    @Provides
    @PerFragment
    @FragmentContext
    fun provideFragmentContext(reviewListFragment: ReviewListFragment): Context? = reviewListFragment.context

    @Provides
    @PerFragment
    fun provideReviewDao(appDatabase: AppDatabase): IReviewDao = appDatabase.provideReviewPickerDao()

    @Provides
    @PerFragment
    fun provideReviewAdapter() = ReviewAdapter()

    @Provides
    @PerFragment
    fun provideLinearLayoutManager(@FragmentContext context: Context?) = LinearLayoutManager(context)
}