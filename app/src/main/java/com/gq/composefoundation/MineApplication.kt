package com.gq.composefoundation

import com.gq.basic.basis.BasicApplication
import com.gq.basic.retrofit.BasicRetrofit
import com.gq.basic.retrofit.RetrofitSSL
import com.gq.composefoundation.retrofit.CustomCallAdapterFactory
import com.gq.composefoundation.retrofit.CustomErrorCodeInterceptor
import com.gq.composefoundation.retrofit.CustomGsonConverterFactory
import dagger.hilt.android.HiltAndroidApp
import java.net.Proxy
import javax.inject.Inject

@HiltAndroidApp
class MineApplication: BasicApplication() {

    @Inject
    lateinit var basicRetrofit: BasicRetrofit
    @Inject
    lateinit var retrofitSSL: RetrofitSSL

    override fun onCreate() {
        super.onCreate()
        basicRetrofit.initialization("https://lovewlever.com/api/", okHttpClientBuilder = {
            val (sslContext, x509) = retrofitSSL.sslFactoryTrust()
            sslSocketFactory(
                sslSocketFactory = sslContext.socketFactory,
                trustManager = x509)
            addInterceptor(CustomErrorCodeInterceptor())
            proxy(Proxy.NO_PROXY)
            this
        }, retrofitBuilder = {
            addCallAdapterFactory(CustomCallAdapterFactory.create())
            addConverterFactory(CustomGsonConverterFactory.create())
        })
    }
}