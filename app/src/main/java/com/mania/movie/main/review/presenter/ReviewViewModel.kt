package com.mania.movie.main.review.presenter

import android.arch.lifecycle.MutableLiveData
import android.os.Environment
import com.mania.movie.MovieManiaApplication
import com.mania.movie.main.base.BaseViewModel
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.mania.movie.main.home.repository.model.MoviePickerModel
import com.mania.movie.main.review.repository.ReviewDbRepository
import com.mania.movie.main.review.repository.ReviewModel
import com.mania.movie.rx.getSingleAsync
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.reactivestreams.Publisher
import java.io.File
import java.io.FileOutputStream


class ReviewViewModel @Inject constructor(application: MovieManiaApplication, private val dbRepository: ReviewDbRepository) : BaseViewModel(application) {

    private val DIRECTORY = Environment.getExternalStorageDirectory().path + "/Reviews/"
    private val picName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    private val storedPath = "$DIRECTORY$picName.png"

    val finishActivity = MutableLiveData<Boolean>()

    init {
        val file = File(DIRECTORY)
        if (!file.exists()) {
            file.mkdir()
        }
    }

    fun saveReview(view: View, width: Int, height: Int, moviePickerModel: MoviePickerModel) =
            getCompositeDisposable().add(
                    saveCanvasToPng(view, width, height)
                            .flatMapCompletable { path ->
                                dbRepository.insertReview(toReviewModel(moviePickerModel, path))
                            }.subscribe({
                                handleOnSuccess()
                            }, {
                                handleOnError(it)
                            })
            )


    private fun saveCanvasToPng(view: View, width: Int, height: Int): Single<String> =
            Single.create<String> { emitter ->
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                val canvas = Canvas(bitmap)
                try {
                    val fileOutStream = FileOutputStream(storedPath)
                    view.draw(canvas)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutStream)
                    fileOutStream.flush()
                    fileOutStream.close()
                    emitter.onSuccess(storedPath)
                } catch (e: Exception) {
                    emitter.onError(Throwable(e))
                }
            }.getSingleAsync(dbRepository.getScheduler())


    private fun toReviewModel(moviePickerModel: MoviePickerModel, storedPath: String) =
            ReviewModel(moviePickerModel.id,
                    moviePickerModel.title,
                    moviePickerModel.year,
                    moviePickerModel.type,
                    moviePickerModel.poster,
                    storedPath)

    private fun handleOnSuccess() {
        finishActivity.postValue(true)
    }


    private fun handleOnError(throwable: Throwable) {
        errorLiveData.postValue(true)
    }


}