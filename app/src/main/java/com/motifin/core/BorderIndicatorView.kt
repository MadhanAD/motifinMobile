package com.motifin.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View

class BorderIndicatorView(context: Context) : View(context) {

    companion object {
        private const val INDICATOR_RADIUS = 30f
    }

    var borderIndicatorModel = BorderIndicatorModel(View(context))
    var onBorderViewActionListener: OnBorderViewActionListener? = null

    private val mPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val mIndicatorPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 12f
        style = Paint.Style.FILL
    }

    fun updateBorder(model: BorderIndicatorModel) {
        borderIndicatorModel = model
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)
        val rectF = borderIndicatorModel.convertToRect()
        canvas.drawRect(
            rectF.left,
            rectF.top,
            rectF.right,
            rectF.bottom,
            mPaint
        )
        canvas.drawCircle(
            rectF.left,
            rectF.top,
            INDICATOR_RADIUS,
            mIndicatorPaint
        )
        canvas.drawCircle(
            rectF.right,
            rectF.top,
            INDICATOR_RADIUS,
            mIndicatorPaint
        )
        canvas.drawCircle(
            rectF.right,
            rectF.bottom,
            INDICATOR_RADIUS,
            mIndicatorPaint
        )
        canvas.drawCircle(
            rectF.left,
            rectF.bottom,
            INDICATOR_RADIUS,
            mIndicatorPaint
        )
        canvas.drawCircle(
            rectF.left / 2 + rectF.right / 2,
            rectF.bottom + rectF.bottom / 8,
            INDICATOR_RADIUS,
            mIndicatorPaint
        )

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val rectF = borderIndicatorModel.convertToRect()
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (isScaleIndicator(event.x, event.y, rectF)) {
                    scaleView()
                } else if (isRotateIndicator(event.x, event.y, rectF)) {
                    rotateView()
                }
                event.actionIndex.also { pointerIndex ->
                    event.getX(pointerIndex)
                }
                event.getPointerId(0)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                true
            }
            MotionEvent.ACTION_UP -> {
                true
            }
            else -> false
        }
    }

    private val scalePoint = 0.1f
    private val rotateAngle = 10
    private fun scaleView() {
        scaleX = scaleX.plus(scalePoint)
        scaleY = scaleY.plus(scalePoint)
        onBorderViewActionListener?.onScaleAction()
    }

    private fun rotateView() {
        rotation = rotation.plus(rotateAngle)
        onBorderViewActionListener?.onRotateAction()
    }

    private fun isScaleIndicator(touchX: Float, touchY: Float, borderRectF: RectF): Boolean {
        // is Left Top
        if (borderRectF.left.plusOrMinusOf(touchX).and(borderRectF.top.plusOrMinusOf(touchY)))
            return true

        // is right top
        if (borderRectF.right.plusOrMinusOf(touchX).and(borderRectF.top.plusOrMinusOf(touchY)))
            return true

        // is right bottom
        if (borderRectF.right.plusOrMinusOf(touchX).and(borderRectF.bottom.plusOrMinusOf(touchY)))
            return true

        // is left bottom
        if (borderRectF.left.plusOrMinusOf(touchX).and(borderRectF.bottom.plusOrMinusOf(touchY)))
            return true

        return false
    }

    private fun isRotateIndicator(touchX: Float, touchY: Float, borderRectF: RectF): Boolean {
        return (borderRectF.left / 2).plus(borderRectF.right / 2).plusOrMinusOf(touchX)
            .and((borderRectF.bottom.plus(borderRectF.bottom / 8)).plusOrMinusOf(touchY))
    }

    interface OnBorderViewActionListener {
        fun onScaleAction()
        fun onRotateAction()
    }
}