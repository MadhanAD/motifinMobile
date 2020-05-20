package com.motifin

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.motifin.core.BorderIndicatorModel
import com.motifin.core.EditorLayout
import com.motifin.core.ResizeImageView


class ResizeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resize)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)

        testWithCustomLayout(frameLayout)
    }

    private var isBorderVisible: Boolean = false

    private fun testWithCustomLayout(frameLayout: FrameLayout) {
        val layout = EditorLayout(this@ResizeActivity)
        val param = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        layout.layoutParams = param

        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.demo_image)

        val imageViewParam = FrameLayout.LayoutParams(1200, 400)
        imageView.x = 125f
        imageView.y = 170f
        imageView.layoutParams = imageViewParam
        imageView.scaleType = ImageView.ScaleType.FIT_XY

        val borderIndicatorModel = BorderIndicatorModel(
            targetView = imageView
        )

        val imageView1 = ImageView(this)
        imageView1.setImageResource(R.drawable.demo_image)

        val imageViewParam1 = FrameLayout.LayoutParams(1200, 400)
        imageView1.x = 125f
        imageView1.y = 620f
        imageView1.layoutParams = imageViewParam1
        imageView1.scaleType = ImageView.ScaleType.FIT_XY

        val borderIndicatorModel1 = BorderIndicatorModel(
            targetView = imageView1
        )

        layout.addView(imageView)
        layout.addView(imageView1)
//        layout.lockChildView(borderIndicatorModel)

        layout.onEditorActionListener = object : EditorLayout.OnEditorActionListener {
            override fun onScale(x: Float, y: Float) {
                imageView.scaleX = imageView.scaleX.plus(0.1f)
                imageView.scaleY = imageView.scaleY.plus(0.1f)
            }

            override fun onRotate(rotateAngle: Float) {
                imageView.rotation = imageView.rotation.plus(10)
            }

            override fun onMove(x: Float, y: Float) {

            }

        }

        val button = Button(this@ResizeActivity)
        button.text = "Update Border"
        val btnParam = ViewGroup.LayoutParams(300, 150)
        button.layoutParams = btnParam
        button.setOnClickListener {
            if (isBorderVisible) {
                if (layout.isViewLocked) layout.unLockChildView()
                layout.lockChildView(borderIndicatorModel)
            } else {
                if (layout.isViewLocked) layout.unLockChildView()
                layout.lockChildView(borderIndicatorModel1)
            }
            isBorderVisible = isBorderVisible.not()
        }

        layout.addView(button)
        frameLayout.addView(layout)
//        frameLayout.addView(button)
    }

    private fun testFunction(frameLayout: FrameLayout) {


//        val customImageView = CustomImageView(this@ResizeActivity)
//        customImageView.setImageResource(R.drawable.demo_image)

        val resizeImageView = ResizeImageView(this@ResizeActivity)
//        resizeImageView.setImageResource(R.drawable.demo_image)
//        resizeImageView.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.demo_image))
        val params = FrameLayout.LayoutParams(700, 500)
//        params.leftMargin = 10
//        params.topMargin = 10
//        params.rightMargin = 10
//        params.bottomMargin = 10
        resizeImageView.layoutParams = params


//        resizeImageView.setInEdit(true)
        frameLayout.addView(resizeImageView)
    }
}
