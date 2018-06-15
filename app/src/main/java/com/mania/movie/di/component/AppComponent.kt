package com.mania.movie.di.component

import android.support.multidex.MultiDexApplication
import com.mania.movie.MovieManiaApplication
import com.mania.movie.di.module.AppModule
import com.mania.movie.di.module.RoomModule
import com.mania.movie.di.scope.PerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@PerApplication
@Component(modules = [(AndroidSupportInjectionModule::class), (AppModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: MultiDexApplication): Builder

        fun appModule(appModule: AppModule): Builder

        fun roomModule(roomModule: RoomModule): Builder

        fun build(): AppComponent
    }

    fun inject(movieManiaApplication: MovieManiaApplication)

}