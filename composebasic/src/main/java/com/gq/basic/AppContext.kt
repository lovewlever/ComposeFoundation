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
        application: Application, ) {
        this.application = application
        // 日志
        if (application.applicationInfo.isApkInDebug()) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberCloseTree())
        }
        Timber.plant(TimberFileTree())
        setDefaultUncaughtExceptionHandler()
    }

    private fun setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppCrashHandler())
    }

    fun isApkInDebug() =
        application.applicationInfo.isApkInDebug()
}