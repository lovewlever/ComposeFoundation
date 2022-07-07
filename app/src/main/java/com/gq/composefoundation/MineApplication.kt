package com.gq.composefoundation

import com.gq.basic.basis.BasicApplication
import com.gq.basic.retrofit.BasicRetrofit
import com.gq.composefoundation.retrofit.CustomCallAdapterFactory
import com.gq.composefoundation.retrofit.CustomErrorCodeInterceptor
import com.gq.composefoundation.retrofit.CustomGsonConverterFactory
import com.gq.composefoundation.retrofit.RetrofitSSLCommon
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

@HiltAndroidApp
class MineApplication: BasicApplication() {

    @Inject
    lateinit var basicRetrofit: BasicRetrofit

    override fun onCreate() {
        super.onCreate()
        basicRetrofit.initialization("https://lovewlever.com/api/", okHttpClientBuilder = {
            val (sslContext, x509) = RetrofitSSLCommon.sslContext()
            sslSocketFactory(
                sslSocketFactory = sslContext.socketFactory,
                trustManager = x509)
            addInterceptor(CustomErrorCodeInterceptor())
            this
        }, retrofitBuilder = {
            addCallAdapterFactory(CustomCallAdapterFactory.create())
            addConverterFactory(CustomGsonConverterFactory.create())
        })
    }
}