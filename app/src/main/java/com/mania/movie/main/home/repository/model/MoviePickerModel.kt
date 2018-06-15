package com.mania.movie.main.home.repository.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.databinding.BaseObservable
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mania.movie.room.IMoviePickerDao

@Entity(tableName = IMoviePickerDao.TABLE_NAME, indices = [(Index("imdbID", unique = true)), (Index("userId"))])
data class MoviePickerModel constructor(

        @PrimaryKey(autoGenerate = false)
        @SerializedName("imdbID")
        @ColumnInfo(name = "imdbID")
        var id: String = "",

        @SerializedName("Title")
        @ColumnInfo(name = "title")
        var title: String = "",

        @SerializedName("Year")
        @ColumnInfo(name = "year")
        var year: String = "",

        @SerializedName("Type")
        @ColumnInfo(name = "Type")
        var type: String = "",

        @SerializedName("Poster")
        @ColumnInfo(name = "Poster")
        var poster: String = "",

        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "createdAt")
        var createdAt: Long = System.currentTimeMillis()) : BaseObservable(), Parcelable {

    var isLoggedIn = false

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
        isLoggedIn = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(year)
        parcel.writeString(type)
        parcel.writeString(poster)
        parcel.writeString(userId)
        parcel.writeByte(if (isLoggedIn) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoviePickerModel> {
        override fun createFromParcel(parcel: Parcel): MoviePickerModel {
            return MoviePickerModel(parcel)
        }

        override fun newArray(size: Int): Array<MoviePickerModel?> {
            return arrayOfNulls(size)
        }
    }

}