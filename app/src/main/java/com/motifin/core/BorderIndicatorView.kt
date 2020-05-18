package com.motifin.core

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class BorderIndicatorView(context: Context) : View(context) {

    companion object {
        private const val INDICATOR_RADIUS = 30f
    }

    var borderIndicatorModel = BorderIndicatorModel(View(context))

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
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                event.actionIndex.also {pointerIndex ->
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
}