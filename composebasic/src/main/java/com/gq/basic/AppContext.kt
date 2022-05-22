package com.gq.basic

import android.app.Application
import android.content.pm.ApplicationInfo
import com.gq.basic.common.TimberCloseTree
import com.gq.basic.extension.isApkInDebug
import com.gq.basic.widget.TimberFileTree
import timber.log.Timber

object AppContext {

    lateinit var application: Application

    fun initialization(application: Application) {
        this.application = application
        // 日志
        if (application.applicationInfo.isApkInDebug()) {
            Timber.plant(TimberFileTree())
        } else {
            Timber.plant(TimberCloseTree())
        }
    }

    fun isApkInDebug() =
        application.applicationInfo.isApkInDebug()
}