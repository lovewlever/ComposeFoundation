package com.gq.basic.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

/**
 * 进度拦截器
 */
class ProgressInterceptor(private val progressListener: ProgressListener):
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(ProgressResponseBody(chain.request().url.toUrl().toString(),
                originalResponse.body, progressListener))
            .build()
    }
}