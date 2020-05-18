package com.motifin.core

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.motifin.R

class CustomBoard(context: Context) : ViewGroup(context) {

    init {
        setBackgroundColor(Color.LTGRAY)
    }

    fun addImageChild(data: BoardImageComponent) {
        val imageView = ImageView(context)
        val layoutParams = LayoutParams(data.width, data.height)

        imageView.layoutParams = layoutParams
        imageView.x = data.x.toFloat()
        imageView.y = data.y.toFloat()
        imageView.setImageDrawable(context.getDrawable(R.drawable.demo_image))
        addView(imageView)
    }

    override fun onLayout(p0: Boolean, start: Int, top: Int, right: Int, bottom: Int) {

    }

}

class DummyView(context: Context) : View(context) {
    private val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.demo_image)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBitmap(bitmap, -100f, -1000f, null)
    }
}

class DummyImageView(context: Context): AppCompatImageView(context) {
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}