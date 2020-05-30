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
    var isMovable = false

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
                //lock pointer
                val indicatorType = getIndicatorType(event.x, event.y, rectF)
                if (indicatorType != IndicatorType.NONE && indicatorType != IndicatorType.ROTATE) {
                    lastTouchX = event.x
                    lastTouchY = event.y
                } else if (indicatorType == IndicatorType.ROTATE) {
//                    rotateView(event)
                }
                isMovable = indicatorType == IndicatorType.NONE

                true
            }
            MotionEvent.ACTION_MOVE -> {
                val indicatorType = getIndicatorType(event.x, event.y, rectF)
                if (indicatorType != IndicatorType.NONE && indicatorType != IndicatorType.ROTATE) {
                    scaleView(event, indicatorType)
                } else if (indicatorType == IndicatorType.ROTATE) {
                    rotateView(event)
                } else if (indicatorType == IndicatorType.NONE) {
                    if (isMovable) moveView(event)
                }
                true
            }
            MotionEvent.ACTION_UP -> {
                true
            }
            else -> false
        }
    }

    private val scalePoint = 0.1f
    private val rotateAngle = -10
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private fun scaleView(motionEvent: MotionEvent, indicatorType: IndicatorType) {
        var x = 0f
        var y = 0f
//        val (x, y) = arrayListOf(scaleX.plus(scalePoint), scaleY.plus(scalePoint))
        when (indicatorType) {
            IndicatorType.LEFT_TOP -> {
                if (lastTouchX > motionEvent.x && lastTouchY > motionEvent.y) {
                    // scale +
                    x = scaleX.plus(scalePoint)
                    y = scaleY.plus(scalePoint)
                } else {
                    // scale -
                    x = scaleX.minus(scalePoint)
                    y = scaleY.minus(scalePoint)
                }
            }
            IndicatorType.RIGHT_TOP -> {
                if (lastTouchX < motionEvent.x && lastTouchY > motionEvent.y) {
                    // scale +
                    x = scaleX.plus(scalePoint)
                    y = scaleY.plus(scalePoint)
                } else {
                    // scale -
                    x = scaleX.minus(scalePoint)
                    y = scaleY.minus(scalePoint)
                }
            }
            IndicatorType.RIGHT_BOTTOM -> {
                if (lastTouchX < motionEvent.x && lastTouchY < motionEvent.y) {
                    // scale +
                    x = scaleX.plus(scalePoint)
                    y = scaleY.plus(scalePoint)
                } else {
                    // scale -
                    x = scaleX.minus(scalePoint)
                    y = scaleY.minus(scalePoint)
                }
            }
            IndicatorType.LEFT_BOTTOM -> {
                if (lastTouchX > motionEvent.x && lastTouchY < motionEvent.y) {
                    // scale +
                    x = scaleX.plus(scalePoint)
                    y = scaleY.plus(scalePoint)
                } else {
                    // scale -
                    x = scaleX.minus(scalePoint)
                    y = scaleY.minus(scalePoint)
                }
            }
            else -> {
                x = 0f
                y = 0f
            }
        }
        val log = "lastTouchX: $lastTouchX ; lastTouchY: $lastTouchY " +
                "\n touchX: ${motionEvent.x} ; touchY: ${motionEvent.y}" +
                "\n VIEW $indicatorType X=$x ; Y=$y"
        Log.e("MESSAGE", log)

        if (x > 0 && y > 0) {
            scaleX = x
            scaleY = y
            onBorderViewActionListener?.onScaleAction(x, y)
            lastTouchX = motionEvent.x
            lastTouchY = motionEvent.y
        }
        invalidate()
    }

    private fun rotateView(event: MotionEvent) {
//        val deltaX = event.x
//        val deltaY = event.y
//        val radians = atan2(deltaY, deltaX)
//        val degree = Math.toDegrees(radians.toDouble())
//
//        val delta = degree - rotation
//        rotation = rotation.plus(delta).toFloat()

        val angle = rotation.minus(rotateAngle)
        rotation = angle
        onBorderViewActionListener?.onRotateAction(angle)
    }

    private fun moveView(event: MotionEvent) {
        val left = event.rawX
        val top = event.rawY - height

        x = left
        y = top
    }

    private enum class IndicatorType {
        NONE, LEFT_TOP, RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM, ROTATE
    }

    private fun getIndicatorType(touchX: Float, touchY: Float, borderRectF: RectF): IndicatorType {
        // is Left Top
        if (borderRectF.left.plusOrMinusOf(touchX).and(borderRectF.top.plusOrMinusOf(touchY)))
            return IndicatorType.LEFT_TOP

        // is right top
        if (borderRectF.right.plusOrMinusOf(touchX).and(borderRectF.top.plusOrMinusOf(touchY)))
            return IndicatorType.RIGHT_TOP

        // is right bottom
        if (borderRectF.right.plusOrMinusOf(touchX).and(borderRectF.bottom.plusOrMinusOf(touchY)))
            return IndicatorType.RIGHT_BOTTOM

        // is left bottom
        if (borderRectF.left.plusOrMinusOf(touchX).and(borderRectF.bottom.plusOrMinusOf(touchY)))
            return IndicatorType.LEFT_BOTTOM

        if ((borderRectF.left / 2).plus(borderRectF.right / 2).plusOrMinusOf(touchX)
                .and((borderRectF.bottom.plus(borderRectF.bottom / 8)).plusOrMinusOf(touchY))
        )
            return IndicatorType.ROTATE

        return IndicatorType.NONE
    }

//    private fun isRotateIndicator(touchX: Float, touchY: Float, borderRectF: RectF): Boolean {
//        return
//    }

    fun updateBorderAfterRotate() {
        // TODO: update border view border according to target view border
    }

    interface OnBorderViewActionListener {
        fun onScaleAction(x: Float, y: Float)
        fun onRotateAction(rotateDegree: Float)
    }
}