package com.mania.movie.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mania.movie.main.home.repository.model.MoviePickerModel
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface IMoviePickerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(moviePickerModel: MoviePickerModel)


    @Query("SELECT * FROM $TABLE_NAME WHERE $USER_ID = :userID ORDER BY $CREATED_AT DESC")
    fun getBookmarks(userID: String): Single<List<MoviePickerModel>>

    companion object {
        const val TABLE_NAME = "book_mark"
        const val USER_ID = "userId"
        const val CREATED_AT = "createdAt"
    }


}