package com.mania.movie.main.bookmark.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.FrameLayout
import com.mania.movie.BR
import com.mania.movie.R
import com.mania.movie.binding.*
import com.mania.movie.databinding.FragmentBookmarkMovieBinding
import com.mania.movie.main.base.BaseFragment
import com.mania.movie.main.bookmark.presenter.BookmarkMovieViewModel
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.mvvm.observe
import com.mania.movie.ui.BindedMultiStateView
import javax.inject.Inject

class BookmarkMoviesFragment : BaseFragment<FragmentBookmarkMovieBinding, BookmarkMovieViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var bookmarkMovieViewModel: BookmarkMovieViewModel

    @Inject
    lateinit var bookmarkAdapter: BookmarkAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fragmentBookmarkMovieBinding: FragmentBookmarkMovieBinding

    private val recyclerViewConfiguration = RecyclerViewConfiguration()

    private val emptyViewConfiguration = EmptyViewConfiguration()

    private val errorViewConfiguration = ErrorViewConfiguration()

    private val contentLoadingConfiguration = ContentLoadingConfiguration()

    private val toolbarConfiguration = ToolbarConfiguration()

    override fun getViewModel() =
            ViewModelProviders.of(this@BookmarkMoviesFragment, viewFactory)
                    .get(BookmarkMovieViewModel::class.java).also {
                        bookmarkMovieViewModel = it
                    }

    override fun getLayoutId() = R.layout.fragment_bookmark_movie

    override fun executePendingVariablesBinding() {
        getViewDataBinding().also {
            it.setVariable(BR.bookmarkMovieViewModel, bookmarkMovieViewModel)
            it.setVariable(BR.recyclerViewConfig, recyclerViewConfiguration)
            it.setVariable(BR.errorViewConfig, errorViewConfiguration)
            it.setVariable(BR.emptyViewConfig, emptyViewConfiguration)
            it.setVariable(BR.contentLoadingViewConfig, contentLoadingConfiguration)
            it.setVariable(BR.toolbarConfig, toolbarConfiguration)
            fragmentBookmarkMovieBinding = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeToBookMarkLiveData()
    }

    private fun initUI() {

        toolbarConfiguration.setToolbarConfig(getString(R.string.str_book_mark),
                ContextCompat.getColor(context!!, R.color.white), 0, onNavigationBackClickListener,
                ContextCompat.getDrawable(context!!, R.drawable.vc_cross_white), null, R.color.colorPrimaryDark)

        emptyViewConfiguration.setEmptyViewConfig(getString(R.string.str_no_movies_available),
                ContextCompat.getDrawable(context!!, R.mipmap.ic_launcher))

        errorViewConfiguration.setErrorViewConfig(getString(R.string.str_something_wrong_msg),
                ContextCompat.getDrawable(context!!, R.mipmap.ic_launcher), onRetryClickListener)

        contentLoadingConfiguration.setConfig(getString(R.string.str_loading))

        bookmarkAdapter.navigator = navigator

        recyclerViewConfiguration.setRecyclerConfig(bookmarkAdapter, linearLayoutManager)

    }

    private val onRetryClickListener = View.OnClickListener {
        bookmarkMovieViewModel.retryGetBookmarkMovies.postValue(true)
    }

    private val onNavigationBackClickListener = View.OnClickListener {
        fragmentTransactionListener?.onFragmentInteraction(View.GONE)
    }

    private val navigator = object : IBookNavigator {
        override fun onMoviePick(moviePickerModel: MoviePickerModel) {
        }
    }

    private fun subscribeToBookMarkLiveData() {
        bookmarkMovieViewModel.bookmarkMovieListLiveData.observe(this) {
            it?.let {
                if (it.isNotEmpty()) {
                    bookmarkAdapter.setData(it)
                    fragmentBookmarkMovieBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_CONTENT)
                } else {
                    fragmentBookmarkMovieBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_EMPTY)
                }
            }
        }
    }

    companion object {
        const val TAG = "BookmarkMoviesFragment"
    }

}