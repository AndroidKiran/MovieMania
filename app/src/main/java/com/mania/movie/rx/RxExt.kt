package com.mania.movie.rx

import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.reactivestreams.Publisher

fun <T> Flowable<T>.getFlowableAsync(schedulerProvider: BaseSchedulerProvider): Flowable<T> =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Single<T>.getSingleAsync(schedulerProvider: BaseSchedulerProvider): Single<T> =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Observable<T>.getObservableAsync(schedulerProvider: BaseSchedulerProvider): Observable<T> =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun Completable.getCompletableAsync(schedulerProvider: BaseSchedulerProvider): Completable =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Publisher<T>.toLiveData() =
        LiveDataReactiveStreams.fromPublisher(this)