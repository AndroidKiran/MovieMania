package com.mania.movie.main.authentication.di

import android.content.Context
import com.mania.movie.di.qualifier.ActivityContext
import com.mania.movie.di.scope.PerActivity
import com.mania.movie.main.authentication.ui.AuthenticationActivity
import dagger.Module
import dagger.Provides


@Module
class AuthenticationModule {

    @Provides
    @PerActivity
    @ActivityContext
    fun provideActivityContext(authenticationActivity: AuthenticationActivity): Context = authenticationActivity

}