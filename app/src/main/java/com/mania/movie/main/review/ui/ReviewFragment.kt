package com.mania.movie.main.review.ui

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.mania.movie.BR
import com.mania.movie.R
import com.mania.movie.binding.*
import com.mania.movie.databinding.FragmentReviewBinding
import com.mania.movie.extension.showSnackBar
import com.mania.movie.main.base.BaseFragment
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.main.home.ui.MoviePickerActivity.Companion.MOVIE_PICKER_MODEL
import com.mania.movie.main.review.presenter.ReviewViewModel
import com.mania.movie.mvvm.observe
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class ReviewFragment : BaseFragment<FragmentReviewBinding, ReviewViewModel>(), EasyPermissions.PermissionCallbacks, IReviewBoardUpdateListener {

    @Inject
    lateinit var viewFactory: ViewModelProvider.Factory

    @Inject
    lateinit var reviewViewModel: ReviewViewModel

    private lateinit var fragmentReviewBinding: FragmentReviewBinding

    private val toolbarConfiguration = ToolbarConfiguration()

    private var moviePickerModel: MoviePickerModel? = null

    override fun getViewModel() =
            ViewModelProviders.of(this@ReviewFragment, viewFactory)
                    .get(ReviewViewModel::class.java).also {
                        reviewViewModel = it
                    }

    override fun getLayoutId() = R.layout.fragment_review

    override fun executePendingVariablesBinding() {
        getViewDataBinding().also {
            it.setVariable(BR.reviewViewModel, reviewViewModel)
            it.setVariable(BR.toolbarConfig, toolbarConfiguration)
            fragmentReviewBinding = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviePickerModel = arguments?.getParcelable(MOVIE_PICKER_MODEL)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeToErrorLiveData()
        subscribeToFinishActivityLiveData()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this@ReviewFragment)

    override fun undoAvailable(boolean: Boolean) {
        val visibility = if (boolean) View.VISIBLE else View.GONE
        fragmentReviewBinding.btnUndo.visibility = visibility
        fragmentReviewBinding.btnSave.visibility = visibility
    }

    override fun redoAvailable(boolean: Boolean) {
        val visibility = if (boolean) View.VISIBLE else View.GONE
        fragmentReviewBinding.btnRedo.visibility = visibility
    }

    private fun initUI() {

        toolbarConfiguration.setToolbarConfig(getString(R.string.str_review),
                ContextCompat.getColor(context!!, R.color.white), 0, toolbarClickListener,
                ContextCompat.getDrawable(context!!, R.drawable.vc_cross_white), null)
        val reviewBoard = ReviewBoard(context!!, null)
        reviewBoard.setReviewBoardUpdateListener(this@ReviewFragment)

        val canvasLayout = fragmentReviewBinding.canvasLayout
        canvasLayout.addView(reviewBoard, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        fragmentReviewBinding.btnRedo.setOnClickListener {
            reviewBoard.onClickRedo()
        }

        fragmentReviewBinding.btnUndo.setOnClickListener {
            reviewBoard.onClickUndo()
        }

        fragmentReviewBinding.btnClear.setOnClickListener {
            reviewBoard.onClickClear()
        }

        fragmentReviewBinding.btnSave.setOnClickListener {
            moviePickerModel?.let {
                seekExternalStoragePermission(canvasLayout as View, canvasLayout.width, canvasLayout.height, it)
            }
        }
    }

    private val toolbarClickListener = View.OnClickListener {
        fragmentTransactionListener?.onFragmentInteraction(View.GONE)
    }

    @AfterPermissionGranted(RC_WRITE_EXT_PER)
    private fun seekExternalStoragePermission(view: View, width: Int, height: Int, moviePickerModel: MoviePickerModel) {
        if (EasyPermissions.hasPermissions(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            try {
                if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                    reviewViewModel.saveReview(view, width, height, moviePickerModel)
                } else {
                    snackBarConfiguration.showSnackBar(getString(R.string.str_cannot_save),
                            SnackbarConfiguration.Type.NEUTRAL,
                            Snackbar.LENGTH_SHORT)
                }
            } catch (execption: RuntimeException) {
                snackBarConfiguration.showSnackBar(getString(R.string.str_cannot_save),
                        SnackbarConfiguration.Type.NEUTRAL,
                        Snackbar.LENGTH_SHORT)
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.perm_ext),
                    RC_WRITE_EXT_PER, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun subscribeToFinishActivityLiveData() {
        reviewViewModel.finishActivity.observe(this) {
            it?.let {
                fragmentTransactionListener?.onFragmentInteraction(View.GONE)
            }
        }
    }

    private fun subscribeToErrorLiveData() {
        reviewViewModel.errorLiveData.observe(this) {
            it?.let {
                snackBarConfiguration.showSnackBar(getString(R.string.str_something_wrong_msg),
                        SnackbarConfiguration.Type.NEUTRAL,
                        Snackbar.LENGTH_SHORT)
            }
        }
    }


    companion object {
        const val TAG = "review_fragment"
        const val RC_WRITE_EXT_PER = 101
    }

}