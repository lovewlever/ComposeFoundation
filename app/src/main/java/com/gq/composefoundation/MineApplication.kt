package com.gq.composefoundation

import com.gq.basic.basis.BasicApplication
import com.gq.basic.retrofit.BasicRetrofit
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MineApplication: BasicApplication() {

    @Inject
    lateinit var basicRetrofit: BasicRetrofit

    override fun onCreate() {
        super.onCreate()
        basicRetrofit.initialization("https://baidu.com/")
    }
}