package com.mania.movie

import android.app.Activity
import android.support.multidex.MultiDexApplication
import com.mania.movie.di.component.DaggerAppComponent
import com.mania.movie.di.module.AppModule
import com.mania.movie.di.module.RoomModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class MovieManiaApplication: MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .roomModule(RoomModule(this))
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}