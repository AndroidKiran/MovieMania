package com.mania.movie.di

import android.arch.lifecycle.ViewModelProvider
import com.mania.movie.di.scope.PerActivity
import com.mania.movie.main.authentication.di.AuthenticationModule
import com.mania.movie.main.authentication.di.AuthenticationProviderModule
import com.mania.movie.main.authentication.ui.AuthenticationActivity
import com.mania.movie.main.home.di.MoviePickerModule
import com.mania.movie.main.home.di.MoviePickerProviderModule
import com.mania.movie.main.home.ui.MoviePickerActivity
import com.mania.movie.mvvm.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector(modules = [(AuthenticationModule::class), (AuthenticationProviderModule::class)])
    abstract fun bindAuthenticationActivity(): AuthenticationActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(MoviePickerModule::class), (MoviePickerProviderModule::class)])
    abstract fun bindHomeActivity(): MoviePickerActivity

}
