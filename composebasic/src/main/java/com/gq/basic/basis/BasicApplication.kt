package com.gq.basic.basis

import android.app.Application
import com.gq.basic.AppContext
import dagger.hilt.android.HiltAndroidApp

open class BasicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.initialization(this)
    }
}