package com.mania.movie.main.authentication.di

import android.arch.lifecycle.ViewModel
import com.mania.movie.di.module.ViewModelKey
import com.mania.movie.main.authentication.presenter.AuthenticationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthenticationProviderModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    abstract fun bindAuthenticationViewModel(viewModel: AuthenticationViewModel): ViewModel

}