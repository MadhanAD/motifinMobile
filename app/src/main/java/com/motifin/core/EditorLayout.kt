package com.motifin.core

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.FrameLayout

class EditorLayout(context: Context) : FrameLayout(context) {

    /*
        1. lock/unlock child by click action on child
        2. Draw frame around locked child
        3. Handle touch gesture in layout that applies to locked child
            3.1 add scale option in gesture on locked child
            3.2 add move option in gesture on locked child
            3.3 add rotate option in gesture on locked child
        4. update coordinates via callbacks
     */

    private val mPaint = Paint()
    private var mBorderView: BorderIndicatorView = BorderIndicatorView(context)
    var isViewLocked: Boolean = false

    private val mOnBorderViewActionListener =
        object : BorderIndicatorView.OnBorderViewActionListener {
            override fun onScaleAction(x: Float, y: Float) {
                onEditorActionListener?.onScale(x, y)
            }

            override fun onRotateAction(rotateDegree: Float) {
                onEditorActionListener?.onRotate(rotateDegree)
            }
        }

    init {
        mPaint.strokeWidth = 10f
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mBorderView.updateBorder(BorderIndicatorModel(View(context)))
        mBorderView.onBorderViewActionListener = mOnBorderViewActionListener
    }

    var onEditorActionListener: OnEditorActionListener? = null

    val onBorderViewChangeListener: OnBorderViewChangeListener by lazy {
        object : OnBorderViewChangeListener {
            override fun updateAfterRotate() {
                mBorderView.updateBorderAfterRotate()
            }
        }
    }

    fun lockChildView(model: BorderIndicatorModel) {
//        mBorderView = BorderIndicatorView(context)
        mBorderView.updateBorder(model)
        mBorderView.layoutParams = model.layoutParam()
        mBorderView.x = model.getBorderX()
        mBorderView.y = model.getBorderY()
        addView(mBorderView)
        isViewLocked = true
    }

    fun unLockChildView(model: BorderIndicatorModel) {
        mBorderView.updateBorder(model)
        mBorderView.layoutParams = model.layoutParam()
        mBorderView.x = model.getBorderX()
        mBorderView.y = model.getBorderY()
        removeView(mBorderView)
        isViewLocked = false
    }


    interface OnEditorActionListener {
        fun onScale(x: Float, y: Float)
        fun onRotate(rotateAngle: Float)
        fun onMove(x: Float, y: Float)
    }

    interface OnBorderViewChangeListener {
        fun updateAfterRotate()
    }
}