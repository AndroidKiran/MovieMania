package com.mania.movie.main.review.repository

import com.mania.movie.room.IReviewDao
import com.mania.movie.rx.SchedulerProvider
import com.mania.movie.rx.getCompletableAsync
import com.mania.movie.rx.getFlowableAsync
import com.mania.movie.rx.toLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class ReviewDbRepository @Inject constructor(private val reviewDao: IReviewDao, private val schedulerProvider: SchedulerProvider) {

    fun insertReview(reviewModel: ReviewModel) =
            Completable.fromCallable {
                reviewDao.insert(reviewModel)
            }.getCompletableAsync(schedulerProvider)


    fun getReviews(userID: String) =
            reviewDao.getReviews(userID)
                    .toFlowable()
                    .getFlowableAsync(schedulerProvider)
                    .onErrorResumeNext(Flowable.empty())
                    .toLiveData()

    fun getScheduler() = schedulerProvider

}