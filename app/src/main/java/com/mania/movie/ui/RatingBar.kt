package com.mania.movie.ui

import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageButton
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.mania.movie.R

class RatingBar constructor(private val anchor: View, private val layout: View) : PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT),
        View.OnClickListener {


    private var ratingSelectionListener: RatingSelectionListener? = null


    internal fun setAnchor() {
        this.showAsDropDown(anchor)
    }

    internal fun setRatingListener(ratingSelectionListener: RatingSelectionListener?) {
        this.ratingSelectionListener = ratingSelectionListener

        val context = if (ratingSelectionListener != null) this@RatingBar else null

        layout.findViewById<AppCompatImageButton>(R.id.smily1).apply {
            setOnClickListener(context)
        }
        layout.findViewById<AppCompatImageButton>(R.id.smily2).apply {
            setOnClickListener(context)
        }
        layout.findViewById<AppCompatImageButton>(R.id.smily3).apply {
            setOnClickListener(context)
        }
        layout.findViewById<AppCompatImageButton>(R.id.smily4).apply {
            setOnClickListener(context)
        }
        layout.findViewById<AppCompatImageButton>(R.id.smily5).apply {
            setOnClickListener(context)
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            val imageButton = it as AppCompatImageButton
            ratingSelectionListener?.onRatingSelected(imageButton.drawable)
            dismiss()
        }
    }

    interface RatingSelectionListener {

        fun onRatingSelected(drawable: Drawable)
    }
}