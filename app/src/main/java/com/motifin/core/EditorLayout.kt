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
    private val mBorderView: BorderIndicatorView = BorderIndicatorView(context)
    var isViewLocked: Boolean = false

    init {
        mPaint.strokeWidth = 10f
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mBorderView.updateBorder(BorderIndicatorModel(View(context)))
        mBorderView.onBorderViewActionListener =
            object : BorderIndicatorView.OnBorderViewActionListener {
                override fun onScaleAction() {
                    onEditorActionListener?.onScale(0f, 0f)
                }

                override fun onRotateAction() {
                    onEditorActionListener?.onRotate(10f)
                }

            }
    }

    var onEditorActionListener: OnEditorActionListener? = null

    fun lockChildView(model: BorderIndicatorModel) {
        mBorderView.updateBorder(model)
        mBorderView.layoutParams = model.layoutParam()
        mBorderView.x = model.getBorderX()
        mBorderView.y = model.getBorderY()
        addView(mBorderView)
        isViewLocked = true
    }

    fun unLockChildView() {
        removeView(mBorderView)
        isViewLocked = false
    }


    interface OnEditorActionListener {
        fun onScale(x: Float, y: Float)
        fun onRotate(rotateAngle: Float)
        fun onMove(x: Float, y: Float)
    }
}