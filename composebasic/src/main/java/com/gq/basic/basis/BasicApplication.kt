package com.gq.basic.basis

import android.app.Application
import com.gq.basic.AppContext
import com.gq.basic.retrofit.BasicRetrofit
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

open class BasicApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.initialization(this)
    }
}