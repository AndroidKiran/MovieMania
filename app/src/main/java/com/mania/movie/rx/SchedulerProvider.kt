package com.mania.movie.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SchedulerProvider @Inject constructor() : BaseSchedulerProvider {

    override fun io(): Scheduler = Schedulers.io()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

}
