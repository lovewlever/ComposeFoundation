package com.gq.basic.widget

import timber.log.Timber
import kotlin.system.exitProcess

/**
 * 崩溃记录
 */
class AppCrashHandler: Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        Timber.e(e)
        exitProcess(0)
    }
}