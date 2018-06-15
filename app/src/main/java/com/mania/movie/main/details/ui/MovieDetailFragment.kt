package com.mania.movie.main.details.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.FrameLayout
import com.mania.movie.BR
import com.mania.movie.R
import com.mania.movie.binding.ToolbarConfiguration
import com.mania.movie.databinding.FragmentMovieDetailsBinding
import com.mania.movie.main.base.BaseFragment
import com.mania.movie.main.details.presenter.MovieDetailsViewModel
import com.mania.movie.mvvm.observe
import javax.inject.Inject

class MovieDetailFragment: BaseFragment<FragmentMovieDetailsBinding, MovieDetailsViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var movieDetailsViewModel: MovieDetailsViewModel

    private lateinit var fragmentMovieDetailsBinding: FragmentMovieDetailsBinding

    private val toolbarConfiguration = ToolbarConfiguration()

    private var movieId: String? = ""

    override fun getViewModel() =
            ViewModelProviders.of(this@MovieDetailFragment, viewFactory)
                    .get(MovieDetailsViewModel::class.java).also {
                        movieDetailsViewModel = it
                    }

    override fun getLayoutId() = R.layout.fragment_movie_details

    override fun executePendingVariablesBinding() {
        getViewDataBinding().also {
            it.setVariable(BR.movieDetailsViewModel, movieDetailsViewModel)
            it.setVariable(BR.toolbarConfig,toolbarConfiguration)
            it.setLifecycleOwner(this)
            fragmentMovieDetailsBinding = it
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = if(savedInstanceState == null) {
            arguments?.getString(MOVIE_ID, "")
        } else {
            savedInstanceState.getString(MOVIE_ID, "")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        movieDetailsViewModel.queryLiveData.postValue(movieId)
        subscribeToMovieDetailsLiveData()
    }

    private fun initUI() {
        toolbarConfiguration.setToolbarConfig("",
                ContextCompat.getColor(context!!, R.color.white), 0, toolbarClickListener,
                ContextCompat.getDrawable(context!!, R.drawable.vc_cross_white), null, android.R.color.transparent)

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(MOVIE_ID, movieId)
    }

    private fun subscribeToMovieDetailsLiveData() {
        movieDetailsViewModel.movieDetailLiveData.observe(this) {
            it?.let {
                fragmentMovieDetailsBinding.movieDetailsModel = it
            }
        }
        movieDetailsViewModel.loadingBinding.set(false)
    }

    private val toolbarClickListener = View.OnClickListener {
        fragmentTransactionListener?.onFragmentInteraction(View.GONE)
    }

    companion object {
        const val MOVIE_ID = "movie_id"
        fun start(context: Context, movieId: String) = Intent(context, MovieDetailFragment::class.java).apply {
            putExtra(MOVIE_ID, movieId)
        }

    }
}