package com.mania.movie.di.module

import android.content.Context
import com.mania.movie.MovieManiaApplication
import com.mania.movie.di.ActivityBuilderModule
import com.mania.movie.di.qualifier.AppContext
import com.mania.movie.di.scope.PerApplication
import com.mania.movie.helper.PreferenceHelper
import dagger.Module
import dagger.Provides

@Module(includes = [(ActivityBuilderModule::class),(NetworkModule::class), (RoomModule::class)])
class AppModule constructor(val application: MovieManiaApplication){

    @Provides
    @PerApplication
    fun provideApplication(): MovieManiaApplication = application

    @Provides
    @PerApplication
    @AppContext
    fun provideContext(): Context = application.applicationContext

    @Provides
    @PerApplication
    fun provideSharedPreference(@AppContext context: Context): PreferenceHelper = PreferenceHelper(context)

}