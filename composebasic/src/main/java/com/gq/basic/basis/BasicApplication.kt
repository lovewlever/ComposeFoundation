package com.gq.basic.basis

import android.app.Application
import com.gq.basic.AppContext
import com.gq.basic.widget.AppCrashHandler

open class BasicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.initialization(this)
        Thread.setDefaultUncaughtExceptionHandler(AppCrashHandler(callback = { t, e -> uncaughtException(t, e) }))
    }
    open fun uncaughtException(t: Thread, e: Throwable) { }
}