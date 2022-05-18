package com.gq.basic.common

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import com.gq.basic.AppContext


object FloatingWindowCommon {

    /**
     * 检查是否打开悬浮窗
     */
    @Deprecated("use checkAppOps2()")
    fun checkAppOps(): Boolean {
        return Settings.canDrawOverlays(AppContext.application)
    }

    /**
     * 检查是否打开悬浮窗
     */
    fun checkAppOps2(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            Settings.canDrawOverlays(AppContext.application)
        } else {
            if (Settings.canDrawOverlays(AppContext.application)) return true
            try {
                val mgr = AppContext.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                //getSystemService might return null
                val viewToAdd = View(AppContext.application)
                val params = WindowManager.LayoutParams(
                    0,
                    0,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT
                )
                viewToAdd.layoutParams = params
                mgr.addView(viewToAdd, params)
                mgr.removeView(viewToAdd)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }
    }


    fun getSettingsIntent(context: Context): Intent {
        return Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.packageName)
        )
    }

    fun showFloatingView(view: View) {
        val windowManager = AppContext.application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val wmParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT or WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW
        )
        windowManager.addView(view, wmParams)
    }

}