package com.mania.movie.main.review.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mania.movie.databinding.ItemReviewBinding
import com.mania.movie.main.base.BaseViewHolder
import com.mania.movie.main.review.repository.ReviewModel

class ReviewAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var reviewList = mutableListOf<ReviewModel>()

    private var itemNavigator: IReviewNavigator? = null

    var navigator: IReviewNavigator?
        get() = itemNavigator
        set(navigator) {
            if (itemNavigator == null) {
                itemNavigator = navigator
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val reviewItemBinding = ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ReviewItemViewHolder(reviewItemBinding)
    }

    override fun getItemCount() = reviewList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun setData(list: List<ReviewModel>) {
        reviewList = list as ArrayList<ReviewModel>
        this.notifyDataSetChanged()
    }

    inner class ReviewItemViewHolder constructor(private val binding: ItemReviewBinding) : BaseViewHolder(binding.root) {

        override fun onBind(position: Int) {
            val reviewModelItem = reviewList[position]
            binding.apply {
                this.reviewModelItem = reviewModelItem
                this.executePendingBindings()
            }
        }
    }

}