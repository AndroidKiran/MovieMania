package com.mania.movie.binding

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView

class GridRecyclerViewConfiguration : BaseObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("gridRecyclerBinding")
        fun bindGridRecyclerViewConfiguration(recyclerView: RecyclerView?, recyclerViewConfig: GridRecyclerViewConfiguration?) {
            recyclerView?.setHasFixedSize(true)
            recyclerView?.layoutManager = recyclerViewConfig?.layoutManager
            recyclerView?.adapter = recyclerViewConfig?.recyclerAdapter
            recyclerViewConfig?.itemDecoration?.let {
                recyclerView?.addItemDecoration(it)
            }
        }
    }

    @get:Bindable
    var layoutManager: GridLayoutManager? = null
        private set(value) {
            field = value
        }

    @get:Bindable
    var recyclerAdapter: RecyclerView.Adapter<*>? = null
        private set(value) {
            field = value
        }

    @get:Bindable
    var itemDecoration: RecyclerView.ItemDecoration? = null
        private set(value) {
            field = value
        }

    fun newState(recyclerAdapter: RecyclerView.Adapter<*>?): Builder = Builder(recyclerAdapter)

    inner class Builder constructor(private val recyclerAdapter: RecyclerView.Adapter<*>?) {

        private var layoutManager: GridLayoutManager? = null

        private var itemDecoration:RecyclerView.ItemDecoration? = null

        fun setLayoutManger(layoutManager: GridLayoutManager?): Builder {
            this.layoutManager = layoutManager
            return this@Builder
        }

        fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration?): Builder {
            this.itemDecoration = itemDecoration
            return  this@Builder
        }

        fun commit() {
            this@GridRecyclerViewConfiguration.setRecyclerConfig(recyclerAdapter, layoutManager, itemDecoration)
        }

    }

    fun setRecyclerConfig(recyclerAdapter: RecyclerView.Adapter<*>?, layoutManager: GridLayoutManager?, itemDecoration: RecyclerView.ItemDecoration?) {
        this.layoutManager = layoutManager
        this.recyclerAdapter = recyclerAdapter
        this.itemDecoration = itemDecoration
        notifyChange()
    }

}

