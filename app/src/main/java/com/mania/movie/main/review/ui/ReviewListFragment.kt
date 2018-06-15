package com.mania.movie.main.review.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mania.movie.BR
import com.mania.movie.R
import com.mania.movie.binding.*
import com.mania.movie.databinding.FragmentReviewListBinding
import com.mania.movie.main.base.BaseFragment
import com.mania.movie.main.review.presenter.ReviewListViewModel
import com.mania.movie.mvvm.observe
import com.mania.movie.ui.BindedMultiStateView
import javax.inject.Inject

class ReviewListFragment : BaseFragment<FragmentReviewListBinding, ReviewListViewModel>() {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var reviewListViewModel: ReviewListViewModel

    @Inject
    lateinit var reviewAdapter: ReviewAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var fragmentReviewListBinding: FragmentReviewListBinding

    private val recyclerViewConfiguration = RecyclerViewConfiguration()

    private val emptyViewConfiguration = EmptyViewConfiguration()

    private val errorViewConfiguration = ErrorViewConfiguration()

    private val contentLoadingConfiguration = ContentLoadingConfiguration()

    private val toolbarConfiguration = ToolbarConfiguration()

    override fun getViewModel() =
            ViewModelProviders.of(this@ReviewListFragment, viewFactory)
                    .get(ReviewListViewModel::class.java).also {
                        reviewListViewModel = it
                    }

    override fun getLayoutId() = R.layout.fragment_review_list

    override fun executePendingVariablesBinding() {
        getViewDataBinding().also {
            it.setVariable(BR.reviewListViewModel, reviewListViewModel)
            it.setVariable(BR.recyclerViewConfig, recyclerViewConfiguration)
            it.setVariable(BR.errorViewConfig, errorViewConfiguration)
            it.setVariable(BR.emptyViewConfig, emptyViewConfiguration)
            it.setVariable(BR.contentLoadingViewConfig, contentLoadingConfiguration)
            it.setVariable(BR.toolbarConfig, toolbarConfiguration)
            fragmentReviewListBinding = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeToReviewListLiveData()
    }

    private fun initUI() {

        toolbarConfiguration.setToolbarConfig(getString(R.string.str_reviews),
                ContextCompat.getColor(context!!, R.color.white), 0, onNavigationBackClickListener,
                ContextCompat.getDrawable(context!!, R.drawable.vc_cross_white), null, R.color.colorPrimary)

        emptyViewConfiguration.setEmptyViewConfig(getString(R.string.str_no_reviews),
                ContextCompat.getDrawable(context!!, R.mipmap.ic_launcher))

        errorViewConfiguration.setErrorViewConfig(getString(R.string.str_something_wrong_msg),
                ContextCompat.getDrawable(context!!, R.mipmap.ic_launcher), onRetryClickListener)

        contentLoadingConfiguration.setConfig(getString(R.string.str_loading))

        reviewAdapter.navigator = navigator

        recyclerViewConfiguration.setRecyclerConfig(reviewAdapter, linearLayoutManager)

    }

    private val onRetryClickListener = View.OnClickListener {
        reviewListViewModel.retryGetReviews.postValue(true)
    }

    private val onNavigationBackClickListener = View.OnClickListener {
        fragmentTransactionListener?.onFragmentInteraction(View.GONE)
    }

    private val navigator = object : IReviewNavigator {

    }

    private fun subscribeToReviewListLiveData() {
        reviewListViewModel.reviewListLiveData.observe(this) {
            it?.let {
                if (it.isNotEmpty()) {
                    reviewAdapter.setData(it)
                    fragmentReviewListBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_CONTENT)
                } else {
                    fragmentReviewListBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_EMPTY)
                }
            }
        }
    }

    companion object {
        const val TAG = "review_list_fragment"
        const val REVIEW_LINEAR_LAYOUT_MANAGER = "review_linear_layout_manager"
    }

}