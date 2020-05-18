package com.motifin.core

import java.io.Serializable


/**
 * Created by Abner on 15/6/11.
 * QQ 230877476
 * Email nimengbo@gmail.com
 */
class StickerPropertyModel : Serializable {
    //贴纸id
    var stickerId: Long = 0

    //文本
    var text: String? = null

    //x坐标
    private var xLocation = 0f

    //y坐标
    private var yLocation = 0f

    //角度
    var degree = 0f

    //缩放值
    var scaling = 0f

    //气泡顺序
    var order = 0

    //水平镜像 1镜像 2未镜像
    var horizonMirror = 0

    //贴纸PNG URL
    var stickerURL: String? = null

    fun getxLocation(): Float {
        return xLocation
    }

    fun setxLocation(xLocation: Float) {
        this.xLocation = xLocation
    }

    fun getyLocation(): Float {
        return yLocation
    }

    fun setyLocation(yLocation: Float) {
        this.yLocation = yLocation
    }

    companion object {
        private const val serialVersionUID = 3800737478616389410L
    }
}