package com.gq.composefoundation.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.UnsupportedEncodingException

/**
 * Okhttp日志打印
 */
class CustomHttpLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        HttpLoggingInterceptor { message ->
            try {
                //val text = URLDecoder.decode(message, "utf-8")
                Timber.i(message)
            } catch (e: UnsupportedEncodingException) {
                Timber.e(e)
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }.intercept(chain)

}



