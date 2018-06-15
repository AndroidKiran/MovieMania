package com.mania.movie.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.main.review.repository.ReviewModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface IReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reviewModel: ReviewModel)


    @Query("SELECT * FROM $TABLE_NAME WHERE $USER_ID = :userID ORDER BY $CREATED_AT DESC")
    fun getReviews(userID: String): Single<List<ReviewModel>>

    companion object {
        const val TABLE_NAME = "review"
        const val USER_ID = "userId"
        const val CREATED_AT = "createdAt"
    }

}