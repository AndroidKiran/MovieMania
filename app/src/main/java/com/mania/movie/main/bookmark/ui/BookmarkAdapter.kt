package com.mania.movie.main.bookmark.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mania.movie.databinding.ItemBookmarkBinding
import com.mania.movie.main.base.BaseViewHolder
import com.mania.movie.main.home.repository.model.MoviePickerModel

class BookmarkAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var bookmarkList = mutableListOf<MoviePickerModel>()

    private var itemNavigator: IBookNavigator? = null

    var navigator: IBookNavigator?
        get() = itemNavigator
        set(navigator) {
            if (itemNavigator == null) {
                itemNavigator = navigator
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val bookmarkBindingItem = ItemBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return BookmarkItemViewHolder(bookmarkBindingItem)
    }

    override fun getItemCount() = bookmarkList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun setData(list: List<MoviePickerModel>) {
        bookmarkList = list as ArrayList<MoviePickerModel>
        this.notifyDataSetChanged()
    }

    inner class BookmarkItemViewHolder constructor(private val binding: ItemBookmarkBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val moviePickerModelItem = bookmarkList[position]
            binding.apply {
                this.moviePickerModelItem = moviePickerModelItem
                this.executePendingBindings()
                this.root.setOnClickListener {
                    itemNavigator?.onMoviePick(moviePickerModelItem)
                }
            }
        }

    }

}