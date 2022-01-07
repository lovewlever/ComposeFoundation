package com.gq.composefoundation.retrofit

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.UnsupportedEncodingException

/**
 * Okhttp日志打印
 */
object CustomHttpLoggingInterceptor {

    fun printResponse() =
        HttpLoggingInterceptor { message ->
            try {
                //val text = URLDecoder.decode(message, "utf-8")
                Timber.i(message)
            } catch (e: UnsupportedEncodingException) {
                Timber.e(e)
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}



