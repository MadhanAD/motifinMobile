package com.motifin.core

import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.MotionEventCompat
import com.motifin.R
import kotlin.math.*


class CustomImageView(context: Context) : AppCompatImageView(context) {

    private lateinit var mBitmap: Bitmap

    // Indicator Bitmap for all corners
    private lateinit var mLeftTopIndicatorBitmap: Bitmap
    private lateinit var mRightTopIndicatorBitmap: Bitmap
    private lateinit var mLeftBottomIndicatorBitmap: Bitmap
    private lateinit var mRightBottomIndicatorBitmap: Bitmap

    private var mIndicatorBitmapWidth: Int = 0
    private var mIndicatorBitmapHeight: Int = 0

    private val mLeftTopIndicatorRect: Rect = Rect()
    private val mRightTopIndicatorRect: Rect = Rect()
    private val mLeftBottomIndicatorRect: Rect = Rect()
    private val mRightBottomIndicatorRect: Rect = Rect()

    private var mLocalPaint: Paint = Paint()

    private var mScreenWidth: Int = 0
    private var mScreenHeight: Int = 0
    private lateinit var mDisplayMetrics: DisplayMetrics
    private var MIN_SCALE: Float = 0.5f
    private var MAX_SCALE: Float = 1.2f
    private var mOriginalWidth: Float = 0f
    private var mHalfDiagonalLength: Double = 0.0

    private var mStickerId: Long = 12
    private var isHorizonMirror = false
    private var mIsInEdit: Boolean = false
    private var mIsInResize: Boolean = false
    private var mLastRotateDegree: Int = 0
    private var mLastLength: Int = 0


    private val mMidPoint: PointF = PointF()

    companion object {
        private const val BITMAP_SCALE: Float = 0.7f
    }

    init {
        mLocalPaint.color = resources.getColor(R.color.colorRed, null)
        mLocalPaint.isAntiAlias = true
        mLocalPaint.isDither = true
        mLocalPaint.style = Paint.Style.STROKE
        mLocalPaint.strokeWidth = 2.0f

        mDisplayMetrics = resources.displayMetrics
        mScreenWidth = mDisplayMetrics.widthPixels
        mScreenHeight = mDisplayMetrics.heightPixels
    }

    override fun onDraw(canvas: Canvas) {
        val arrayOfFloat = FloatArray(9)
        matrix.getValues(arrayOfFloat)
        val f1 =
            0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2]
        val f2 =
            0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5]
        val f3 =
            arrayOfFloat[0] * mBitmap.width + 0.0f * arrayOfFloat[1] + arrayOfFloat[2]
        val f4 =
            arrayOfFloat[3] * mBitmap.width + 0.0f * arrayOfFloat[4] + arrayOfFloat[5]
        val f5 =
            0.0f * arrayOfFloat[0] + arrayOfFloat[1] * mBitmap.height + arrayOfFloat[2]
        val f6 =
            0.0f * arrayOfFloat[3] + arrayOfFloat[4] * mBitmap.height + arrayOfFloat[5]
        val f7 =
            arrayOfFloat[0] * mBitmap.width + arrayOfFloat[1] * mBitmap.height + arrayOfFloat[2]
        val f8 =
            arrayOfFloat[3] * mBitmap.width + arrayOfFloat[4] * mBitmap.height + arrayOfFloat[5]

        canvas.save()
        canvas.drawBitmap(mBitmap, matrix, null)

        mLeftTopIndicatorRect.left = (f3 - mLeftTopIndicatorBitmap.width / 2).toInt()
        mLeftTopIndicatorRect.right = (f3 + mLeftTopIndicatorBitmap.width / 2).toInt()
        mLeftTopIndicatorRect.top = (f4 - mLeftTopIndicatorBitmap.width / 2).toInt()
        mLeftTopIndicatorRect.bottom = (f4 + mLeftTopIndicatorBitmap.width / 2).toInt()

        mRightTopIndicatorRect.left = (f7 - mRightTopIndicatorBitmap.width / 2).toInt()
        mRightTopIndicatorRect.right = (f7 + mRightTopIndicatorBitmap.width / 2).toInt()
        mRightTopIndicatorRect.top = (f8 - mRightTopIndicatorBitmap.width / 2).toInt()
        mRightTopIndicatorRect.bottom = (f8 + mRightTopIndicatorBitmap.width / 2).toInt()

        mRightBottomIndicatorRect.left = (f1 - mRightBottomIndicatorBitmap.width / 2).toInt()
        mRightBottomIndicatorRect.right = (f1 + mRightBottomIndicatorBitmap.width / 2).toInt()
        mRightBottomIndicatorRect.top = (f2 - mRightBottomIndicatorBitmap.width / 2).toInt()
        mRightBottomIndicatorRect.bottom = (f2 + mRightBottomIndicatorBitmap.width / 2).toInt()

        mLeftBottomIndicatorRect.left = (f5 - mLeftBottomIndicatorBitmap.width / 2).toInt()
        mLeftBottomIndicatorRect.right = (f5 + mLeftBottomIndicatorBitmap.width / 2).toInt()
        mLeftBottomIndicatorRect.top = (f6 - mLeftBottomIndicatorBitmap.width / 2).toInt()
        mLeftBottomIndicatorRect.bottom = (f6 + mLeftBottomIndicatorBitmap.width / 2).toInt()

        if (mIsInEdit) {
            canvas.drawLine(f1, f2, f3, f4, mLocalPaint)
            canvas.drawLine(f3, f4, f7, f8, mLocalPaint)
            canvas.drawLine(f5, f6, f7, f8, mLocalPaint)
            canvas.drawLine(f5, f6, f1, f2, mLocalPaint)
            canvas.drawBitmap(mLeftTopIndicatorBitmap, null, mLeftTopIndicatorRect, null)
            canvas.drawBitmap(mRightTopIndicatorBitmap, null, mRightTopIndicatorRect, null)
            canvas.drawBitmap(mRightBottomIndicatorBitmap, null, mRightBottomIndicatorRect, null)
            canvas.drawBitmap(mLeftBottomIndicatorBitmap, null, mLeftBottomIndicatorRect, null)
        }

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = MotionEventCompat.getActionMasked(event)
        val handled = true
        when (action) {
            MotionEvent.ACTION_DOWN ->
//                if (isInButton(event!!, dst_delete)) {
//                if (operationListener != null) {
//                    operationListener.onDeleteClick()
//                }
//            }
                if (isInResize(event)) {
                    mIsInResize = true
                    mLastRotateDegree = rotationToStartPoint(event).roundToInt()
                    midPointToStartPoint(event)
                    mLastLength = diagonalLength(event).roundToInt()
                }
//         else if (isInButton(event, dst_flipV)) {
            //水平镜像
//                val localPointF = PointF()
//                midDiagonalPoint(localPointF)
//                matrix.postScale(-1.0f, 1.0f, localPointF.x, localPointF.y)
//                isHorizonMirror = !isHorizonMirror
//                invalidate()
//            }
//        else if (isInButton(event, dst_top)) {

//                bringToFront()
//                if (operationListener != null) {
//                    operationListener.onTop(this)
//                }
//            } else if (isInBitmap(event)) {
//                isInSide = true
//                lastX = event.getX(0)
//                lastY = event.getY(0)
//            } else {
//                handled = false
//            }
            MotionEvent.ACTION_POINTER_DOWN -> {
//                if (spacing(event!!) > pointerLimitDis) {
//                    oldDis = spacing(event)
//                    isPointerDown = true
//                    midPointToStartPoint(event)
//                } else {
//                    isPointerDown = false
//                }
//                isInSide = false
//                isInResize = false
            }
            MotionEvent.ACTION_MOVE ->                 //双指缩放
            {
                matrix.postRotate(
                    (rotationToStartPoint(event!!) - mLastRotateDegree) * 2,
                    mMidPoint.x,
                    mMidPoint.y
                )
                mLastRotateDegree = rotationToStartPoint(event).roundToInt()
                var scale: Float = diagonalLength(event) / mLastLength
                if (diagonalLength(event) / mHalfDiagonalLength <= MIN_SCALE && scale < 1 ||
                    diagonalLength(event) / mHalfDiagonalLength >= MAX_SCALE && scale > 1
                ) {
                    scale = 1f
                    if (!isInResize(event)) {
                        mIsInResize = false
                    }
                } else {
                    mLastLength = diagonalLength(event).roundToInt()
                }
                matrix.postScale(scale, scale, mMidPoint.x, mMidPoint.y)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
            }
        }
        return handled
    }

    private fun initBitmaps() {

        if (mBitmap.width >= mBitmap.height) {
            val minWidth = mScreenWidth.div(8)
            MIN_SCALE = if (mBitmap.width < minWidth) 1f else 1.0f * minWidth / mBitmap.width

            MAX_SCALE =
                if (mBitmap.width > mScreenWidth) 1f else 1.0f * mScreenWidth / mBitmap.width

        } else {
            val minHeight = mScreenWidth.div(8)
            MIN_SCALE = if (mBitmap.height < minHeight) 1f else 1.0f * minHeight / mBitmap.height
            MAX_SCALE =
                if (mBitmap.height > mScreenWidth) 1f else 1.0f * mScreenWidth / mBitmap.height
        }

        mLeftTopIndicatorBitmap = getBitMapFromDrawable(R.drawable.ic_circle_white)
        mRightTopIndicatorBitmap = getBitMapFromDrawable(R.drawable.ic_circle_white)
        mRightBottomIndicatorBitmap = getBitMapFromDrawable(R.drawable.ic_circle_white)
        mLeftBottomIndicatorBitmap = getBitMapFromDrawable(R.drawable.ic_circle_white)

        mIndicatorBitmapWidth = mLeftTopIndicatorBitmap.width.times(BITMAP_SCALE).roundToInt()
        mIndicatorBitmapHeight = mLeftTopIndicatorBitmap.height.times(BITMAP_SCALE).roundToInt()


    }

    private fun getBitMapFromDrawable(drawableId: Int): Bitmap {
        val drawable = resources.getDrawable(drawableId, null)
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
    }

    override fun setImageResource(resId: Int) {
        setBitMap(BitmapFactory.decodeResource(resources, resId))
    }

    fun setBitMap(bitmap: Bitmap) {
        matrix.reset()
        mBitmap = bitmap
        setDiagonalLength()
        initBitmaps()

        val w = mBitmap.width
        val h = mBitmap.height
        mOriginalWidth = w.toFloat()
        val initScale = (MIN_SCALE + MAX_SCALE) / 2
        matrix.postScale(initScale, initScale, (w / 2).toFloat(), (h / 2).toFloat())

        matrix.postTranslate(
            (mScreenWidth / 2 - w / 2).toFloat(),
            ((mScreenHeight) / 2 - h / 2).toFloat()
        )
        invalidate()

    }

    private fun setDiagonalLength() {
        mHalfDiagonalLength = hypot(mBitmap.width.toDouble(), mBitmap.height.toDouble()) / 2
    }

    // utility
    private fun calculate(model: StickerPropertyModel): StickerPropertyModel {
        val floatArray = FloatArray(9)
        matrix.getValues(floatArray)

        val tx = floatArray[Matrix.MTRANS_X]
        val ty = floatArray[Matrix.MTRANS_Y]

        val scaleX = floatArray[Matrix.MSCALE_X]
        val skewY = floatArray[Matrix.MSKEW_Y]
        val rScale =
            Math.sqrt((scaleX * scaleX + skewY * skewY).toDouble()).toFloat()

        // calculate the degree of rotation

        // calculate the degree of rotation
        val rAngle = (atan2(
            floatArray[Matrix.MSKEW_X].toDouble(),
            floatArray[Matrix.MSCALE_X].toDouble()
        ) * (180 / Math.PI)).roundToInt()

        val localPointF = PointF()
        midDiagonalPoint(localPointF)

        val minX = localPointF.x
        val minY = localPointF.y

        model.degree = Math.toRadians(rAngle.toDouble()).toFloat()
        val precentWidth: Float = mBitmap.width * rScale / mScreenWidth
        model.scaling = precentWidth
        model.setxLocation(minX / mScreenWidth)
        model.setyLocation(minY / mScreenWidth)
        model.stickerId = mStickerId
        if (isHorizonMirror) {
            model.horizonMirror = 1
        } else {
            model.horizonMirror = 2
        }

        return model
    }

    private fun midDiagonalPoint(paramPointF: PointF) {
        val arrayOfFloat = FloatArray(9)
        this.matrix.getValues(arrayOfFloat)
        val f1 =
            0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2]
        val f2 =
            0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5]
        val f3 =
            arrayOfFloat[0] * mBitmap.width + arrayOfFloat[1] * mBitmap.height + arrayOfFloat[2]
        val f4 =
            arrayOfFloat[3] * mBitmap.width + arrayOfFloat[4] * mBitmap.height + arrayOfFloat[5]
        val f5 = f1 + f3
        val f6 = f2 + f4
        paramPointF[f5 / 2.0f] = f6 / 2.0f
    }

    private fun pointInRect(
        xRange: FloatArray,
        yRange: FloatArray,
        x: Float,
        y: Float
    ): Boolean {

        val a1 = hypot(
            xRange[0] - xRange[1].toDouble(),
            yRange[0] - yRange[1].toDouble()
        )
        val a2 = hypot(
            xRange[1] - xRange[2].toDouble(),
            yRange[1] - yRange[2].toDouble()
        )
        val a3 = hypot(
            xRange[3] - xRange[2].toDouble(),
            yRange[3] - yRange[2].toDouble()
        )
        val a4 = hypot(
            xRange[0] - xRange[3].toDouble(),
            yRange[0] - yRange[3].toDouble()
        )

        val b1 =
            hypot(x - xRange[0].toDouble(), y - yRange[0].toDouble())
        val b2 =
            hypot(x - xRange[1].toDouble(), y - yRange[1].toDouble())
        val b3 =
            hypot(x - xRange[2].toDouble(), y - yRange[2].toDouble())
        val b4 =
            hypot(x - xRange[3].toDouble(), y - yRange[3].toDouble())
        val u1 = (a1 + b1 + b2) / 2
        val u2 = (a2 + b2 + b3) / 2
        val u3 = (a3 + b3 + b4) / 2
        val u4 = (a4 + b4 + b1) / 2

        val s = a1 * a2
        val ss = (sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1)))
        return abs(s - ss) < 0.5
    }

    private fun isInButton(event: MotionEvent, rect: Rect): Boolean {
        val left = rect.left
        val right = rect.right
        val top = rect.top
        val bottom = rect.bottom
        return event.getX(0) >= left && event.getX(0) <= right &&
                event.getY(0) >= top && event.getY(0) <= bottom
    }

    private fun isInResize(event: MotionEvent): Boolean {
        val left: Int = -20 + mLeftTopIndicatorRect.left
        val top: Int = -20 + mLeftTopIndicatorRect.top
        val right: Int = 20 + mLeftTopIndicatorRect.right
        val bottom: Int = 20 + mLeftTopIndicatorRect.bottom
        return event.getX(0) >= left && event.getX(0) <= right
                && event.getY(0) >= top && event.getY(0) <= bottom
    }

    private fun midPointToStartPoint(event: MotionEvent) {
        val arrayOfFloat = FloatArray(9)
        matrix.getValues(arrayOfFloat)
        val f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2]
        val f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5]
        val f3 = f1 + event.getX(0)
        val f4 = f2 + event.getY(0)
        mMidPoint.set(f3 / 2, f4 / 2)
    }

    private fun rotationToStartPoint(event: MotionEvent): Float {
        val arrayOfFloat = FloatArray(9)
        matrix.getValues(arrayOfFloat)
        val x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2]
        val y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5]
        val arc = atan2(event.getY(0) - y.toDouble(), event.getX(0) - x.toDouble())
        return Math.toDegrees(arc).toFloat()
    }

    private fun diagonalLength(event: MotionEvent): Float {
        return hypot(
            (event.getX(0) - mMidPoint.x).toDouble(),
            (event.getY(0) - mMidPoint.y).toDouble()
        ).toFloat()
    }

    /**
     * 计算双指之间的距离
     */
    private fun spacing(event: MotionEvent): Float {
        return if (event.pointerCount == 2) {
            val x = event.getX(0) - event.getX(1)
            val y = event.getY(0) - event.getY(1)
            sqrt(x * x + y * y.toDouble()).toFloat()
        } else {
            0f
        }
    }

    fun setInEdit(isInEdit: Boolean) {
        mIsInEdit = isInEdit
        invalidate()
    }

    private fun isInBitmap(event: MotionEvent): Boolean {
        val arrayOfFloat1 = FloatArray(9)
        this.matrix.getValues(arrayOfFloat1)
        //左上角
        val f1 =
            0.0f * arrayOfFloat1[0] + 0.0f * arrayOfFloat1[1] + arrayOfFloat1[2]
        val f2 =
            0.0f * arrayOfFloat1[3] + 0.0f * arrayOfFloat1[4] + arrayOfFloat1[5]
        //右上角
        val f3 =
            arrayOfFloat1[0] * mBitmap.width + 0.0f * arrayOfFloat1[1] + arrayOfFloat1[2]
        val f4 =
            arrayOfFloat1[3] * mBitmap.width + 0.0f * arrayOfFloat1[4] + arrayOfFloat1[5]
        //左下角
        val f5 =
            0.0f * arrayOfFloat1[0] + arrayOfFloat1[1] * mBitmap.height + arrayOfFloat1[2]
        val f6 =
            0.0f * arrayOfFloat1[3] + arrayOfFloat1[4] * mBitmap.height + arrayOfFloat1[5]
        //右下角
        val f7 =
            arrayOfFloat1[0] * mBitmap.width + arrayOfFloat1[1] * mBitmap.height + arrayOfFloat1[2]
        val f8 =
            arrayOfFloat1[3] * mBitmap.width + arrayOfFloat1[4] * mBitmap.height + arrayOfFloat1[5]
        val arrayOfFloat2 = FloatArray(4)
        val arrayOfFloat3 = FloatArray(4)
        //确定X方向的范围
        arrayOfFloat2[0] = f1 //左上的x
        arrayOfFloat2[1] = f3 //右上的x
        arrayOfFloat2[2] = f7 //右下的x
        arrayOfFloat2[3] = f5 //左下的x
        //确定Y方向的范围
        arrayOfFloat3[0] = f2 //左上的y
        arrayOfFloat3[1] = f4 //右上的y
        arrayOfFloat3[2] = f8 //右下的y
        arrayOfFloat3[3] = f6 //左下的y
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0))
    }

}
