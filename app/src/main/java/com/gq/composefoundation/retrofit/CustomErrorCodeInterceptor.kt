package com.gq.composefoundation.retrofit

import com.gq.basic.common.GsonCommon
import com.gq.composefoundation.data.ResultEntity
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * 服务器错误代码拦截器
 */
class CustomErrorCodeInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val proceed = chain.proceed(request = request)
        if (proceed.code != 200) {
            val str = proceed.body?.string()
            val build = proceed.newBuilder()
                .body(GsonCommon.gson.toJson(ResultEntity<String>(code = -1, msg = str ?: ""))
                    .toResponseBody("application/json; charset=utf-8".toMediaType()))
                .code(200)
            return build.build()
        }

        return proceed
    }
}