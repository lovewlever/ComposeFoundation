package com.gq.basic.basis

import android.app.Application
import com.gq.basic.AppContext

open class BasicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.initialization(this)
    }
}