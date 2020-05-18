package com.motifin.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.motifin.R
import kotlin.math.roundToInt

class ResizeImageView(context: Context) : AppCompatImageView(context) {

    //  four bitmap indicator
    //  four Rect indicator
    // draw imageView with indicator and original bitmap
    private lateinit var mBitmap: Drawable

    private lateinit var mLeftTopIndicatorBitmap: Bitmap
    private lateinit var mRightTopIndicatorBitmap: Bitmap
    private lateinit var mLeftBottomIndicatorBitmap: Bitmap
    private lateinit var mRightBottomIndicatorBitmap: Bitmap

    private val mLeftTopIndicatorRect = Rect()
    private val mRightTopIndicatorRect = Rect()
    private val mLeftBottomIndicatorRect = Rect()
    private val mRightBottomIndicatorRect = Rect()

    private var mOriginalWidth: Int = 0
    private var mOriginalHeight: Int = 0
    private val mPaint = Paint()

    init {
        initBitmaps()
        mPaint.strokeWidth = 10f
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val arrayOfFloat = FloatArray(9)
        matrix.getValues(arrayOfFloat)
        Log.e("Draw size ", arrayOfFloat.contentToString())
//        canvas.drawBitmap(mBitmap, matrix, null)
        val rect = canvas.clipBounds
//        rect.bottom = 700
//        rect.left = -700
        mBitmap.bounds = rect
        mBitmap.draw(canvas)


        val param = layoutParams as FrameLayout.LayoutParams

        canvas.drawBitmap(
            mLeftTopIndicatorBitmap,
            null,
            Rect(-60, -60, param.width / 6, param.height / 6),
            null
        )
//        canvas.drawBitmap(mRightTopIndicatorBitmap, x.plus(layoutParams.width), y, null)
//        canvas.drawBitmap(
//            mRightBottomIndicatorBitmap,
//            x.plus(layoutParams.width),
//            y.plus(layoutParams.height),
//            null
//        )
//        canvas.drawBitmap(
//            mLeftBottomIndicatorBitmap,
//            x.plus(layoutParams.width),
//            y.plus(layoutParams.height),
//            null
//        )

        canvas.drawRect(
            left.minus(param.leftMargin).toFloat(),
            top.minus(param.topMargin).toFloat(),
            right.minus(param.rightMargin).toFloat(),
            bottom.minus(param.bottomMargin).toFloat(),
            mPaint
        )


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun initBitmaps() {
        mBitmap = resources.getDrawable(R.drawable.demo_image, null)
        mOriginalWidth = mBitmap.intrinsicWidth
        mOriginalHeight = mBitmap.intrinsicHeight
        mLeftTopIndicatorBitmap = R.drawable.ic_circle_white.xmlDrawableIdToBitmap()
        mRightTopIndicatorBitmap = R.drawable.ic_circle_white.xmlDrawableIdToBitmap()
        mRightBottomIndicatorBitmap = R.drawable.ic_circle_white.xmlDrawableIdToBitmap()
        mLeftBottomIndicatorBitmap = R.drawable.ic_circle_white.xmlDrawableIdToBitmap()

        // calculate the indicator points
        mLeftTopIndicatorRect.left = x.roundToInt()
        mLeftTopIndicatorRect.top = y.roundToInt()
        mLeftTopIndicatorRect.right = x.roundToInt()
        mLeftTopIndicatorRect.bottom = y.roundToInt()


    }


    private fun Int.xmlDrawableIdToBitmap(): Bitmap {
        try {
            val drawable = resources.getDrawable(this, null)
            val canvas = Canvas()
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            canvas.setBitmap(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)

            return bitmap
        } catch (exception: Exception) {
            Log.e("Extension function exception ", exception.toString())
            return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        }
    }

}