package com.mania.movie.rx

import android.arch.lifecycle.LiveDataReactiveStreams
import com.mania.movie.helper.Result
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.reactivestreams.Publisher

fun <T> Flowable<T>.getFlowableAsync(schedulerProvider: BaseSchedulerProvider): Flowable<T> =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Single<T>.getSingleAsync(schedulerProvider: BaseSchedulerProvider): Single<T> =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun Completable.getCompletableAsync(schedulerProvider: BaseSchedulerProvider): Completable =
        this.subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

fun <T> Publisher<T>.toLiveData() =
        LiveDataReactiveStreams.fromPublisher(this)

fun <T> Flowable<T>.apibaseResponseToResult(): Flowable<Result<T>> =
        this.map { it.asResult() }
                .onErrorReturn { return@onErrorReturn it.asErrorResult<T>() }

fun <T> T.asResult(): Result<T> = Result(this, null)

fun <T> Throwable.asErrorResult(): Result<T> = Result(null, this)