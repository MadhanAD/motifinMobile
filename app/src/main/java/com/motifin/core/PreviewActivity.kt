package com.motifin.core

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.motifin.R
import com.motifin.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityPreviewBinding
    private val mViewList: ArrayList<View> = ArrayList()
    private var isLastViewIsImageView = false
    private var isBorderVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.tapToEditTextView.setOnClickListener {
            // open bottomSheet to view its options
            val lastIndex = mViewList.size.minus(1)
            if (lastIndex >= 0) {
                mBinding.editorLayout.removeViewAt(lastIndex)
                mViewList.removeAt(lastIndex)
            }
        }

        mBinding.addViewTextView.setOnClickListener {
            // add view to the list
            if (isLastViewIsImageView) {
                // add TextView
                val textView = TextView(this).apply {
                    setOnClickListener(mOnViewClickListener)
                    val textData = "Text ${mViewList.size}"
                    text = textData
                    layoutParams = FrameLayout.LayoutParams(500, 200)
                }

                mViewList.add(textView)
                mBinding.editorLayout.addView(textView)
            } else {
                // add ImageView
                val imageView = ImageView(this).apply {
                    setOnClickListener(mOnViewClickListener)
                    setImageResource(R.drawable.demo_image)
                    layoutParams = FrameLayout.LayoutParams(1200, 400)
                    x = 125f
                    y = 170f
                    scaleType = ImageView.ScaleType.FIT_XY
                }
                mViewList.add(imageView)
                mBinding.editorLayout.addView(imageView)
            }
            isLastViewIsImageView = isLastViewIsImageView.not()
        }
    }

    private val mOnViewClickListener = View.OnClickListener {

        with(mBinding.editorLayout) {
            if (isViewLocked) {
                currentTargetView?.let { unLockChildView(BorderIndicatorModel(it)) }
            }
            lockChildView(BorderIndicatorModel(it))
        }
    }
}