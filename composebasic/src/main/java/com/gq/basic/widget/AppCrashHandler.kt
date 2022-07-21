package com.gq.basic.widget

import android.os.Build
import timber.log.Timber
import kotlin.system.exitProcess

/**
 * 崩溃记录
 */
class AppCrashHandler(var callback: (t: Thread, e: Throwable) -> Unit) :
    Thread.UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        Timber.e(e)
        val sb = StringBuilder()
            .append("-Android-SDK:${Build.VERSION.SDK_INT}\n")
            .append("-厂商:${Build.BRAND}\n")
            .append("-手机型号:${Build.MODEL}\n")
            .append("-CPU架构:${Build.CPU_ABI}\n")
        Timber.e(sb.toString())
        callback(t, e)
        exitProcess(0)
    }
}