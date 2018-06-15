package com.mania.movie.main.home.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mania.movie.databinding.ItemMoviePickerBinding
import com.mania.movie.helper.PreferenceHelper
import com.mania.movie.helper.Utils
import com.mania.movie.main.base.BaseViewHolder
import com.mania.movie.main.home.repository.model.MoviePickerModel

class MoviePickerAdapter constructor(private val preferenceHelper: PreferenceHelper) : RecyclerView.Adapter<BaseViewHolder>() {

    var movieList = mutableListOf<MoviePickerModel>()

    private var itemNavigator: IMoviePickerNavigator? = null

    var navigator: IMoviePickerNavigator?
        get() = itemNavigator
        set(navigator) {
            if (itemNavigator == null) {
                itemNavigator = navigator
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val moviePickerBinding = ItemMoviePickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return MoviePickerViewHolder(moviePickerBinding)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun setData(list: List<MoviePickerModel>) {
        movieList = list as ArrayList<MoviePickerModel>
        this.notifyDataSetChanged()
    }

    inner class MoviePickerViewHolder constructor(private val binding: ItemMoviePickerBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {

            val moviePickerModel = movieList[position].apply {
                isLoggedIn = Utils.isLoggedIn(preferenceHelper)
            }

            binding.apply {
                this.moviePickerModelItem = moviePickerModel
                this.executePendingBindings()

                if (!moviePickerModel.isLoggedIn) {
                    this.root.setOnClickListener(null)
                } else {
                    this.root.setOnClickListener {
                        itemNavigator?.onMoviePick(moviePickerModel)
                    }
                }

                this.btnLike.setOnClickListener {
                    itemNavigator?.onMovieLike(moviePickerModel, it)
                }

                this.btnReview.setOnClickListener {
                    itemNavigator?.onMovieReview(moviePickerModel)
                }

                this.radioBookmark.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        itemNavigator?.onMovieBookmark(moviePickerModel)
                    }
                }
            }
        }
    }

}