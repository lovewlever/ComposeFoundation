package com.gq.basic.common

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

interface SystemUiController {

    fun isDarkMode(): Boolean

    fun setDefaultUiMode(uiMode: Int)

    fun findStatusBarHeight(): Int

    fun findNavigationBarHeight(): Int

    fun fullScreen()

    fun setDecorFitsSystemWindows(
        decorFitsSystemWindows: Boolean = false
    ): SystemUiController

    fun setStatusBarColor(
        color: Int = Color.TRANSPARENT,
        isLight: Boolean = false
    ): SystemUiController

    fun setNavigationBarColor(
        color: Int = Color.TRANSPARENT
    ): SystemUiController

}

@ActivityScoped
class SystemUiControllerImpl @Inject constructor(
    val activity: Activity
) : SystemUiController {

    override fun isDarkMode(): Boolean {
        return when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            AppCompatDelegate.MODE_NIGHT_NO -> false
            else -> {
                when (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> true
                    else -> false
                }
            }
        }
    }

    override fun setDefaultUiMode(uiMode: Int) {
        when (uiMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }

    override fun findStatusBarHeight(): Int {
        val resourceId =
            activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return activity.resources.getDimensionPixelSize(resourceId)
        }
        return DensityCommon.dip2px(26F)
    }

    override fun findNavigationBarHeight(): Int {
        val resourceId =
            activity.resources.getIdentifier("navigation_bar_height","dimen", "android")
        if (resourceId > 0) {
            return activity.resources.getDimensionPixelSize(resourceId)
        }
        return 0
    }

    override fun fullScreen() {
        activity.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun setDecorFitsSystemWindows(
        decorFitsSystemWindows: Boolean
    ): SystemUiController {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.setDecorFitsSystemWindows(decorFitsSystemWindows)
            activity.window
                .decorView
                .windowInsetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_SWIPE
        } else {
            activity.window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.statusBarColor = Color.TRANSPARENT

            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        return this
    }

    override fun setStatusBarColor(
        color: Int,
        isLight: Boolean
    ): SystemUiController {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = color
        //activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (isLight) {
            setStatusBarIconLightColor()
        } else {
            setStatusBarIconDarkColor()
        }
        return this
    }

    override fun setNavigationBarColor(color: Int): SystemUiController {
        activity.window.navigationBarColor = color
        return this
    }

    /**
     * 状态栏深色Icon
     */
    private fun setStatusBarIconDarkColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window
                .decorView
                .windowInsetsController
                ?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
        } else {

            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            activity.window.decorView.systemUiVisibility =
                activity.window.decorView.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        }
    }

    /**
     * 状态栏亮色Icon
     */
    private fun setStatusBarIconLightColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window
                .decorView
                .windowInsetsController
                ?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
        } else {

            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
            activity.window.navigationBarColor = Color.TRANSPARENT
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.window.decorView.systemUiVisibility =
                    activity.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }

        }
    }

}