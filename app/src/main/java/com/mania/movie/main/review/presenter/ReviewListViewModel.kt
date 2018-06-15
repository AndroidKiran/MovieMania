package com.mania.movie.main.review.presenter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mania.movie.MovieManiaApplication
import com.mania.movie.helper.PreferenceHelper
import com.mania.movie.helper.Utils
import com.mania.movie.main.base.BaseViewModel
import com.mania.movie.main.review.repository.ReviewDbRepository
import com.mania.movie.main.review.repository.ReviewModel
import com.mania.movie.mvvm.switchMap
import javax.inject.Inject


class ReviewListViewModel @Inject constructor(application: MovieManiaApplication, private val dbRepository: ReviewDbRepository, private val preferenceHelper: PreferenceHelper) : BaseViewModel(application) {

    var retryGetReviews = MutableLiveData<Boolean>()

    private val userId = Utils.getUserID(preferenceHelper)!!

    var reviewListLiveData: LiveData<List<ReviewModel>> = retryGetReviews.switchMap {
        when (it) {
            null -> MutableLiveData()
            else -> dbRepository.getReviews(userId)
        }
    }

    init {
        retryGetReviews.postValue(true)
    }

}