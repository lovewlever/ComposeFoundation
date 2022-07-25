package com.gq.basic

import android.app.Application
import com.gq.basic.common.TimberCloseTree
import com.gq.basic.extension.isApkInDebug
import com.gq.basic.widget.AppCrashHandler
import com.gq.basic.widget.TimberFileTree
import timber.log.Timber

object AppContext {

    lateinit var application: Application

    fun initialization(
        application: Application,
        timberFileTree: Boolean = true,
        defaultUncaughtExceptionHandle: Boolean = true,
        uncaughtException: (t: Thread, e: Throwable) -> Unit = { _,_ -> }
    ) {
        this.application = application
        // 日志
        if (application.applicationInfo.isApkInDebug()) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberCloseTree())
        }
        if (timberFileTree) {
            Timber.plant(TimberFileTree())
        }
        if (defaultUncaughtExceptionHandle) {
            setDefaultUncaughtExceptionHandler(uncaughtException)
        }
    }

    private fun setDefaultUncaughtExceptionHandler(uncaughtException: (t: Thread, e: Throwable) -> Unit = { _,_ -> }) {
        Thread.setDefaultUncaughtExceptionHandler(AppCrashHandler(callback = { t, e -> uncaughtException(t, e) }))
    }

    fun isApkInDebug() =
        application.applicationInfo.isApkInDebug()
}