package com.gq.basic.retrofit

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 延迟请求拦截器
 * 防止进入页面时卡顿
 */
class DelayedRequestInterceptor(private val timestamp: Long = 200) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Thread.sleep(timestamp)
        return chain.proceed(chain.request())
    }
}