package com.gq.basic

import android.app.Application
import android.content.pm.ApplicationInfo
import com.gq.basic.common.TimberCloseTree
import timber.log.Timber

object AppContext {

    lateinit var application: Application

    fun initialization(application: Application) {
        this.application = application
        // 日志
        if ((application.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberCloseTree())
        }
    }
}