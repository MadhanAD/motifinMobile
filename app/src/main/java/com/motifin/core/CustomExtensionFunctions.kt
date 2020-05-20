package com.motifin.core


fun Float.plusOrMinusOf(target: Float, range: Float = 30f): Boolean {
    val (min, max) = arrayListOf(this.minus(range), this.plus(range))
    return target in min..max
}