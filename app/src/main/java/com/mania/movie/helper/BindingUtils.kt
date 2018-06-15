package com.mania.movie.helper

import android.databinding.BindingAdapter
import android.support.v7.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions.bitmapTransform


object BindingUtils {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun bindLoadImage(imageView: AppCompatImageView?, url: String?) =
            url?.takeIf { it.isNotEmpty() }?.apply {
                GlideApp.with(imageView?.context!!)
                        .load(this)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView)
            }


    @JvmStatic
    @BindingAdapter(value = ["imageUrlWithCircularTrans"])
    fun bindLoadImageWithCircularTrans(imageView: AppCompatImageView?, url: String?) =
            url?.takeIf { it.isNotEmpty() }?.apply {
                GlideApp.with(imageView?.context!!)
                        .load(this)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .apply(bitmapTransform(CircleCrop()))
                        .into(imageView)
            }

    @JvmStatic
    @BindingAdapter(value = ["imageUrlWithRoundCornerTrans"])
    fun bindLoadImageWithRoundCornerTrans(imageView: AppCompatImageView?, url: String?) =
            url?.takeIf { it.isNotEmpty() }?.apply {
                imageView?.context?.let {
                    Glide.with(it)
                            .load(this)
                            .into(imageView)
                }
            }

}



