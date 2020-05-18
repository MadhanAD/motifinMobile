package com.motifin.core

import android.graphics.RectF
import android.view.View
import android.widget.FrameLayout

fun BorderIndicatorModel.convertToRect(): RectF {
    val rectF = RectF()
    rectF.left = (this.targetView.x - (this.targetView.x * 0.3)).toFloat()
    rectF.top = (this.targetView.y - (this.targetView.y * 0.55)).toFloat()
    rectF.right = rectF.left + this.targetView.layoutParams.width
    rectF.bottom = rectF.top + this.targetView.layoutParams.height
    return rectF
}

fun BorderIndicatorModel.getBorderX(): Float {
    return (this.targetView.x * 0.3).toFloat()
}

fun BorderIndicatorModel.getBorderY(): Float {
    return (this.targetView.y * 0.55).toFloat()
}

fun BorderIndicatorModel.getBorderWidth(): Int {
    return this.targetView.layoutParams.width.plus(200).toInt()
}

fun BorderIndicatorModel.getBorderHeight(): Int {
    return this.targetView.layoutParams.height.plus(200).toInt()
}

fun BorderIndicatorModel.layoutParam(): FrameLayout.LayoutParams {
    return FrameLayout.LayoutParams(this.getBorderWidth(), this.getBorderHeight())
}

data class BorderIndicatorModel(
    var targetView: View
)