package com.gq.composefoundation.retrofit

import com.gq.basic.common.DataStoreCommon
import okhttp3.*
import timber.log.Timber


/**
 * 参数添加TokenAndUid
 */
class CustomAddTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newBuilder = request.newBuilder()
        val token = DataStoreCommon.getBasicTypeBySP("Constants.DSKey.SPToken", "")
        val userId = DataStoreCommon.getBasicTypeBySP("Constants.DSKey.SPUserId", -1)
        if (request.method == "GET") {
            val httpUrl: HttpUrl = request.url
                .newBuilder()
                .addQueryParameter("token", token)
                .addQueryParameter("userId", userId.toString())
                .build()
            newBuilder
                .header("token", token)
                .header("userId", userId.toString())
                .url(httpUrl)
        } else if (request.method == "POST") {
            val bodyBuilder = FormBody.Builder()
            if (request.body is FormBody) {
                val formBody = request.body as FormBody

                for (i in 0 until formBody.size) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i))
                }
            }
            bodyBuilder.addEncoded("token", token)
            bodyBuilder.addEncoded("userId", userId.toString())
            val newBody = bodyBuilder.build()

            for (i in 0 until newBody.size) {
                Timber.i("${newBody.name(i)}  ${newBody.value(i)}")
            }
            newBuilder
                .header("token", token)
                .header("userId", userId.toString())
            newBuilder.post(newBody)
        }

        val response = chain.proceed(newBuilder.build())
        return response
    }
}