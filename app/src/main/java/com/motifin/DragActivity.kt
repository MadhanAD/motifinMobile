package com.motifin

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.motifin.core.BoardImageComponent
import com.motifin.core.CustomBoard
import com.motifin.core.DummyView

class DragActivity : AppCompatActivity() {

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
//    private lateinit var layoutParams: FrameLayout.LayoutParams

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)

        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)

        imageView1.apply {
            x = -100f
            y = 10f
            setOnTouchListener(mOnTouchListener)
//            setOnDragListener(mOnDragListener)
        }

        imageView2.apply {
            x = 100f
            y = 100f
            setOnTouchListener(mOnTouchListener)
//            setOnDragListener(mOnDragListener)
        }
    }

    inner class MyShadowDragBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = ColorDrawable(Color.GRAY)
        override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
            val width = view.width / 2
            val height = view.height / 2
            shadow.setBounds(0, 0, width, height)
            outShadowSize.set(width, height)
            outShadowTouchPoint.set(width / 2, height / 2)
        }

        override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }
    }

    private fun log(message: String) = Log.e("Logger: ", message)
    private val mOnTouchListener = View.OnTouchListener { view, motionEvent ->
        val layoutParams = view.layoutParams as FrameLayout.LayoutParams
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {

//                val clipData = ClipData.newPlainText("", "")
//                val shadowBuilder = MyShadowDragBuilder(view)
//                view.startDragAndDrop(clipData, shadowBuilder, view, 0)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val left = motionEvent.rawX
                val top = motionEvent.rawY - view.height

                log("x: ${motionEvent.rawX} ; y: ${motionEvent.rawY}")
                log("leftMargin: ${layoutParams.leftMargin} ; topMargin: ${layoutParams.topMargin}")
//                view.layoutParams = layoutParams
                view.x = left
                view.y = top

                true
            }
            else -> {
                false
            }
        }
    }

    private val mOnDragListener: View.OnDragListener =
        View.OnDragListener { view, dragEvent ->
            Log.e("Main Activity", "action : ${dragEvent.action}")
            Log.e("Main Activity ", "x: ${dragEvent.x} ; y: ${dragEvent.y} ")
//            val layoutParams: FrameLayout.LayoutParams =  view.layoutParams as FrameLayout.LayoutParams
            val localState = dragEvent.localState as View
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    view.x = dragEvent.x
//                    view.layoutParams = layoutParams
                }
                DragEvent.ACTION_DRAG_ENTERED -> {

                    view.x = dragEvent.x
                    view.y = dragEvent.y
//                    view.layoutParams = layoutParams
                }
                DragEvent.ACTION_DRAG_ENDED -> {
//                    view.layoutParams = layoutParams
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    view.x = dragEvent.x
                    view.y = dragEvent.y
                    view.invalidate()
//                    view.layoutParams = layoutParams
                }
                DragEvent.ACTION_DROP -> {
                    view.x = dragEvent.x
                    view.y = dragEvent.y
//                    view.layoutParams = layoutParams
                }
            }
            true
        }

    private fun getRootView(): View {
        val customBoard = CustomBoard(this@DragActivity)
        customBoard.addImageChild(
            BoardImageComponent(
                x = 50, y = 500, width = 750, height = 500, imageUrl = ""
            )
        )
        val frameLayout = FrameLayout(this@DragActivity)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        frameLayout.layoutParams = params
        val data = BoardImageComponent(
            x = 50, y = 500, width = 750, height = 300, imageUrl = ""
        )
        val imageView = ImageView(this@DragActivity)
        val layoutParams = FrameLayout.LayoutParams(data.width, data.height)
        imageView.layoutParams = layoutParams
        imageView.x = data.x.toFloat()
        imageView.y = data.y.toFloat()
        imageView.setImageDrawable(getDrawable(R.drawable.demo_image))

        frameLayout.addView(DummyView(this@DragActivity))

//        customBoard.layoutParams = params
        return frameLayout
    }
}