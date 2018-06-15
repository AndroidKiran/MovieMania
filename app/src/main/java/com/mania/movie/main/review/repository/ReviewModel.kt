package com.mania.movie.main.review.repository

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.databinding.BaseObservable
import com.mania.movie.room.IReviewDao

@Entity(tableName = IReviewDao.TABLE_NAME, indices = [(Index("imdbID", unique = true)),(Index("userId"))])
data class ReviewModel constructor(

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "imdbID")
        var id: String = "",

        @ColumnInfo(name = "title")
        var title: String = "",

        @ColumnInfo(name = "year")
        var year: String = "",

        @ColumnInfo(name = "Type")
        var type: String = "",

        @ColumnInfo(name = "Poster")
        var poster: String = "",

        @ColumnInfo(name = "review_path")
        var reviewPath: String = "",

        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "createdAt")
        var createdAt: Long = System.currentTimeMillis()) : BaseObservable()

