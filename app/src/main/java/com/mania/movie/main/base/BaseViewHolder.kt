package com.mania.movie.main.base

import android.support.v7.widget.RecyclerView
import android.view.View


abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(position: Int)

}