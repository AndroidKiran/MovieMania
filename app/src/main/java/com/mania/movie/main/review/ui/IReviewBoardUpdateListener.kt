package com.mania.movie.main.review.ui

interface IReviewBoardUpdateListener {

    fun undoAvailable(boolean: Boolean)

    fun redoAvailable(boolean: Boolean)
}