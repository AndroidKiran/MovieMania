package com.mania.movie.main.home.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import com.mania.movie.BR
import com.mania.movie.R
import com.mania.movie.binding.*
import com.mania.movie.databinding.ActivityMoviePickerBinding
import com.mania.movie.extension.replace
import com.mania.movie.extension.showSnackBar
import com.mania.movie.helper.ItemOffsetDecoration
import com.mania.movie.helper.Utils
import com.mania.movie.main.authentication.ui.AuthenticationActivity
import com.mania.movie.main.base.BaseActivity
import com.mania.movie.main.bookmark.ui.BookmarkMoviesFragment
import com.mania.movie.main.details.ui.MovieDetailFragment
import com.mania.movie.main.details.ui.MovieDetailFragment.Companion.MOVIE_ID
import com.mania.movie.main.home.presenter.MoviePickerViewModel
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.main.review.ui.ReviewFragment
import com.mania.movie.main.review.ui.ReviewListFragment
import com.mania.movie.mvvm.observe
import com.mania.movie.ui.BindedMultiStateView
import com.mania.movie.ui.RatingBar
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MoviePickerActivity : BaseActivity<ActivityMoviePickerBinding, MoviePickerViewModel>(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var moviePickerViewModel: MoviePickerViewModel

    @Inject
    lateinit var moviePickerAdapter: MoviePickerAdapter

    @Inject
    lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var itemOffsetDecoration: ItemOffsetDecoration

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var activityMoviePickerBinding: ActivityMoviePickerBinding

    private val gridRecyclerViewConfiguration = GridRecyclerViewConfiguration()

    private val emptyViewConfiguration = EmptyViewConfiguration()

    private val errorViewConfiguration = ErrorViewConfiguration()

    private val contentLoadingConfiguration = ContentLoadingConfiguration()

    private val toolbarConfiguration = ToolbarConfiguration()

    private var ratingBarBar: RatingBar? = null


    override fun getViewModel() =
            ViewModelProviders.of(this@MoviePickerActivity, viewFactory)
                    .get(MoviePickerViewModel::class.java).also {
                        moviePickerViewModel = it
                    }

    override fun getLayoutId() = R.layout.activity_movie_picker

    override fun executePendingVariablesBinding() {
        getViewDataBinding().also {
            it.setVariable(BR.moviePickerViewModel, moviePickerViewModel)
            it.setVariable(BR.gridRecyclerViewConfig, gridRecyclerViewConfiguration)
            it.setVariable(BR.errorViewConfig, errorViewConfiguration)
            it.setVariable(BR.emptyViewConfig, emptyViewConfiguration)
            it.setVariable(BR.contentLoadingViewConfig, contentLoadingConfiguration)
            it.setVariable(BR.snackBarConfig, snackBarConfiguration)
            it.setVariable(BR.toolbarConfig, toolbarConfiguration)
            it.setVariable(BR.editTextListener, editTextListener)
            activityMoviePickerBinding = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        subscribeToMovieListLiveData()
        subscribeToLoggedInLiveData()
        subscribeToErrorLiveData()
        subscribeToSuccessLiveData()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
            val preferenceHelper = moviePickerViewModel.getPreferenceHelper()
            moviePickerViewModel.isLoggedIn.postValue(Utils.isLoggedIn(preferenceHelper))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        val isloggedIn = moviePickerViewModel.isLoggedIn.value

        isloggedIn?.let {
            menu?.findItem(R.id.action_log_in)?.isVisible = !isloggedIn
            menu?.findItem(R.id.action_log_out)?.isVisible = isloggedIn
            menu?.findItem(R.id.action_book_mark)?.isVisible = isloggedIn
            menu?.findItem(R.id.action_review)?.isVisible = isloggedIn
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initUI() {

        toolbarConfiguration.setToolbarConfig(getString(R.string.app_name),
                ContextCompat.getColor(this, R.color.white), R.menu.menu_home, null,
                null, menuClickListener, R.color.colorPrimary)

        emptyViewConfiguration.setEmptyViewConfig(getString(R.string.str_no_movies_available),
                ContextCompat.getDrawable(this, R.mipmap.ic_launcher))

        errorViewConfiguration.setErrorViewConfig(getString(R.string.str_something_wrong_msg),
                ContextCompat.getDrawable(this, R.mipmap.ic_launcher), onRetryClickListener)

        contentLoadingConfiguration.setConfig(getString(R.string.str_loading))

        moviePickerAdapter.navigator = navigator

        gridRecyclerViewConfiguration.setRecyclerConfig(moviePickerAdapter, gridLayoutManager, itemOffsetDecoration)

        setSupportActionBar(activityMoviePickerBinding.homeToolbar?.toolbar)

    }

    private val onRetryClickListener = View.OnClickListener {
        activityMoviePickerBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_LOADING)
        moviePickerViewModel.queryLiveData.postValue("batman")
    }


    private fun subscribeToMovieListLiveData() {
        moviePickerViewModel.movieListLiveData.observe(this) {
            it?.let {
                if (it.isSuccess()) {
                    val movieList = it.value.searchList
                    if (movieList != null && movieList.isNotEmpty()) {
                        moviePickerAdapter.setData(movieList)
                        activityMoviePickerBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_CONTENT)
                    } else {
                        activityMoviePickerBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_EMPTY)
                    }
                } else {
                    activityMoviePickerBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_ERROR)
                }
            }
        }
    }

    private fun subscribeToLoggedInLiveData() {
        moviePickerViewModel.isLoggedIn.observe(this) {
            it?.let {
                if (moviePickerViewModel.queryLiveData.value == null) {
                    moviePickerViewModel.queryLiveData.postValue("batman")
                }
                invalidateOptionsMenu()
            }
        }
    }

    private val navigator = object : IMoviePickerNavigator {

        override fun onMovieLike(moviePickerModel: MoviePickerModel, view: View) {
            hideKeyboard()
            dismissRating()
            ratingBarBar = getRatingView(view)
        }

        override fun onMovieBookmark(moviePickerModel: MoviePickerModel) {
            hideKeyboard()
            dismissRating()
            moviePickerViewModel.insertBookmarkMovie(moviePickerModel)
        }

        override fun onMovieReview(moviePickerModel: MoviePickerModel) {
            hideKeyboard()
            dismissRating()
            replace(ReviewFragment().apply {
                this.arguments = Bundle().apply {
                    putParcelable(MOVIE_PICKER_MODEL, moviePickerModel)
                }
            }, ReviewFragment.TAG, R.id.fragment_container)
            toggleFragment(View.VISIBLE)

        }

        override fun onMoviePick(moviePickerModel: MoviePickerModel) {
            hideKeyboard()
            dismissRating()
            replace(MovieDetailFragment().apply {
                this.arguments = Bundle().apply {
                    putString(MOVIE_ID, moviePickerModel.id)
                }
            }, BookmarkMoviesFragment.TAG, R.id.fragment_container)
            toggleFragment(View.VISIBLE)
        }
    }

    override fun onFragmentInteraction(visible: Int) {
        toggleFragment(visible)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchItem = savedInstanceState?.getString(SEARCH_ITEM, "")
        moviePickerViewModel.queryLiveData.postValue(searchItem)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(SEARCH_ITEM, moviePickerViewModel.queryLiveData.value)
    }

    private val menuClickListener = Toolbar.OnMenuItemClickListener {

        when (it.itemId) {
            R.id.action_book_mark -> {
                hideKeyboard()
                dismissRating()
                replace(BookmarkMoviesFragment(), BookmarkMoviesFragment.TAG, R.id.fragment_container)
                toggleFragment(View.VISIBLE)
            }

            R.id.action_log_in -> {
                hideKeyboard()
                dismissRating()
                startActivityForResult(AuthenticationActivity.start(this), REQUEST_CODE)
            }

            R.id.action_log_out -> {
                hideKeyboard()
                dismissRating()
                startActivityForResult(AuthenticationActivity.start(this), REQUEST_CODE)
            }

            R.id.action_review -> {
                hideKeyboard()
                dismissRating()
                replace(ReviewListFragment(), ReviewListFragment.TAG, R.id.fragment_container)
                toggleFragment(View.VISIBLE)
            }
        }

        return@OnMenuItemClickListener false
    }

    private val editTextListener = object : IEditTextChangeListener {
        override fun afterTextChanged(editable: Editable) {
            activityMoviePickerBinding.multiStateViewLayout?.multiStateView?.setViewState(BindedMultiStateView.VIEW_STATE_LOADING)
            moviePickerViewModel.queryLiveData.postValue(editable.toString())
        }
    }

    private fun toggleFragment(visible: Int) {
        activityMoviePickerBinding.fragmentContainer.visibility = visible
    }

    private fun subscribeToSuccessLiveData() {
        moviePickerViewModel.successLiveData.observe(this) {
            it?.let {
                snackBarConfiguration.showSnackBar(getString(R.string.str_bookmark_success_msg),
                        SnackbarConfiguration.Type.NEUTRAL,
                        Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun subscribeToErrorLiveData() {
        moviePickerViewModel.errorLiveData.observe(this) {
            it?.let {
                snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg),
                        SnackbarConfiguration.Type.NEUTRAL,
                        Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun getRatingView(view: View): RatingBar {
        val inflater = this@MoviePickerActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.layout_rating, null)

        return RatingBar(view, layout).apply {
            setAnchor()
            setRatingListener(ratingListener)
        }
    }

    private val ratingListener = object : RatingBar.RatingSelectionListener {
        override fun onRatingSelected(drawable: Drawable) {
            val drawableItem = drawable
        }
    }


    private fun dismissRating() {
        ratingBarBar?.let {
            it.setRatingListener(null)
            it.dismiss()
        }
    }

    companion object {
        fun start(context: Context) = Intent(context, MoviePickerActivity::class.java)
        const val REQUEST_CODE = 9001
        const val SEARCH_ITEM = "search_item"
        const val MOVIE_PICKER_MODEL = "movie_picker_model"
    }

}