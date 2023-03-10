package com.gq.basic.common

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.Window
import com.gq.basic.AppContext


/**
 * px/dp/sp相互转换工具
 * @author 01218
 * @created 2019/2/21
 */
object DensityCommon {
    private lateinit var outMetrics: DisplayMetrics
    private var screenScale: Float = -1F
    var statusBarHeight: Int = -1
        private set
    var navigationBarHeight: Int = -1
        private set

    @SuppressLint("PrivateApi")
    fun initDisplayMetrics(window: Window) {
        // 状态栏高度
        val c = Class.forName("com.android.internal.R\$dimen")
        val obj = c.newInstance()
        val field = c.getField("status_bar_height")
        val x = field.get(obj)
        statusBarHeight = AppContext.application.resources.getDimensionPixelSize(x as Int)
        // 屏幕宽高信息
        outMetrics = DisplayMetrics()
        screenScale = outMetrics.density
        window.windowManager.defaultDisplay?.getRealMetrics(outMetrics)
        // 底部导航栏高度
        val metrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(metrics);
        val usableHeight = metrics.heightPixels  //跟rect.bottom的值一样
        navigationBarHeight = outMetrics.heightPixels - usableHeight
    }

    private fun checkDisplayMetrics() {
        if (!this::outMetrics.isInitialized || screenScale == -1F) {
            throw UninitializedPropertyAccessException("DisplayMetrics 未初始化")
        }
    }

    /**
     * px 转 dp【按照一定的比例】
     */
    fun px2dipByRatio(pxValue: Float, ratio: Float): Int {
        checkDisplayMetrics()
        val scale = screenScale * ratio
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * dp转px【按照一定的比例】
     */
    fun dip2pxByRatio(dpValue: Float, ratio: Float): Int {
        checkDisplayMetrics()
        val scale = screenScale * ratio
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px 转 dp
     */
    fun px2dip(pxValue: Float): Int {
        checkDisplayMetrics()
        return (pxValue / screenScale + 0.5f).toInt()
    }

    /**
     * dp转px
     */
    fun dip2px(dpValue: Float): Int {
        checkDisplayMetrics()
        return (dpValue * screenScale + 0.5f).toInt()
    }

    /**
     * 获取屏幕的宽度（像素）
     */
    fun getScreenWidth(): Int {
        checkDisplayMetrics()
        return outMetrics.widthPixels
    }

    /**
     * 获取屏幕的宽度（dp）
     */
    fun getScreenWidthDp(): Int {
        checkDisplayMetrics()
        val scale = screenScale
        return (outMetrics.widthPixels / scale).toInt()
    }

    /**
     * 获取屏幕的高度（像素）
     */
    fun getScreenHeight(): Int {
        checkDisplayMetrics()
        return outMetrics.heightPixels
    }

    /**
     * 获取屏幕的高度（像素）
     */
    fun getScreenHeightDp(): Int {
        checkDisplayMetrics()
        val scale = screenScale
        return (outMetrics.heightPixels / scale).toInt()
    }

    /**
     * 指定机型（displayMetrics.xdpi）下dp转px
     */
    fun dpToPxByDevice(dp: Int): Int {
        return Math.round(dp.toFloat() * getPixelScaleFactor())
    }

    /**
     * 指定机型（displayMetrics.xdpi）下px 转 dp
     */
    fun pxToDpByDevice(px: Int): Int {
        return Math.round(px.toFloat() / getPixelScaleFactor())
    }

    /**
     * 获取水平方向的dpi的密度比例值
     * 2.7653186
     */
    fun getPixelScaleFactor(): Float {
        val displayMetrics = AppContext.application.resources.displayMetrics
        return displayMetrics.xdpi / 160.0f
    }
}
