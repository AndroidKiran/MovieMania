package com.mania.movie.main.review.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class ReviewBoard : View {

    private var canvas = Canvas()
    private var path = Path()
    private var paint = Paint()
    private var paths = ArrayList<Path>()
    private var undonePaths = ArrayList<Path>()

    private var xPoint = 0f

    private var yPoint = 0f

    var viewContext: Context? = null

    private var reviewBoardListener: IReviewBoardUpdateListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        this.viewContext = context
        isFocusable = true
        isFocusableInTouchMode = true
        paint.apply {
            this.isAntiAlias = true
            this.isDither = true
            this.color = Color.BLACK
            this.style = Paint.Style.STROKE
            this.strokeJoin = Paint.Join.ROUND
            this.strokeCap = Paint.Cap.ROUND
            this.strokeWidth = 6f
        }
    }

    override fun onDraw(canvas: Canvas) {
        for (p in paths) {
            canvas.drawPath(p, paint)
        }
        canvas.drawPath(path, paint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x!!, y!!)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x!!, y!!)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    override fun onDetachedFromWindow() {
        reviewBoardListener = null
        super.onDetachedFromWindow()
    }

    fun setReviewBoardUpdateListener(context: Fragment) {
        reviewBoardListener = context as IReviewBoardUpdateListener
    }

    fun onClickUndo() {
        if (paths.size > 0) {
            val index = paths.size - 1
            val path = paths[index]
            paths.remove(path)
            undonePaths.add(path)
            invalidate()

            if (undonePaths.size == 1) {
                reviewBoardListener?.redoAvailable(true)
            }

            if (paths.size == 0) {
                reviewBoardListener?.undoAvailable(false)
            }
        } else {
            reviewBoardListener?.undoAvailable(false)

        }
    }

    fun onClickRedo() {
        if (undonePaths.size > 0) {
            val index = undonePaths.size - 1
            val path = undonePaths[index]
            undonePaths.remove(path)
            paths.add(path)
            invalidate()

            if (paths.size == 1) {
                reviewBoardListener?.undoAvailable(true)
            }

        } else {
            reviewBoardListener?.redoAvailable(false)
        }
    }

    fun onClickClear() {
        path.reset()
        paths = ArrayList()
        undonePaths = ArrayList()
        invalidate()
        reviewBoardListener?.redoAvailable(false)
        reviewBoardListener?.undoAvailable(false)
    }



    private fun touchStart(x: Float, y: Float) {
        undonePaths.clear()
        path.reset()
        path.moveTo(x, y)
        xPoint = x
        yPoint = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - xPoint)
        val dy = Math.abs(y - yPoint)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(xPoint, yPoint, (x + xPoint) / 2, (y + yPoint) / 2)
            xPoint = x
            yPoint = y
        }
    }

    private fun touchUp() {
        path.lineTo(xPoint, yPoint)
        canvas.drawPath(path, paint)
        paths.add(path)
        path = Path()
        if (paths.size == 1) {
            reviewBoardListener?.undoAvailable(true)
        }
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }
}