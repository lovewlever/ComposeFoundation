package com.gq.composefoundation.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.UnsupportedEncodingException

/**
 * Okhttp日志打印
 * @see com.gq.basic.retrofit.LogInterceptor
 */
@Deprecated("use LogInterceptor")
class CustomHttpLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        HttpLoggingInterceptor { message ->
            try {
                //val text = URLDecoder.decode(message, "utf-8")
                Timber.e(message)
            } catch (e: UnsupportedEncodingException) {
                Timber.e(e)
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }.intercept(chain)

}



