package com.gq.composefoundation.retrofit

import okhttp3.*
import timber.log.Timber


/**
 * 参数添加TokenAndUid
 */
class CustomAddTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newBuilder = request.newBuilder()
        /*val token = DataStoreCommon.getBasicTypeBySP(Constants.DataStoreKey_Token_BySp, "")
        val userId = DataStoreCommon.getBasicTypeBySP(Constants.DataStoreKey_UserId_BySp, 0)*/
        if (request.method == "GET") {
            val httpUrl: HttpUrl = request.url
                .newBuilder()
                .addQueryParameter("token", "token")
                .addQueryParameter("user_id", "userId.toString()")
                .build()
            newBuilder.url(httpUrl)
        } else if (request.method == "POST") {
            val bodyBuilder = FormBody.Builder()
            if (request.body is FormBody) {
                val formBody = request.body as FormBody

                for (i in 0 until formBody.size) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i))
                }
            }
            bodyBuilder.addEncoded("token", "token")
            bodyBuilder.addEncoded("user_id", "userId.toString()")
            val newBody = bodyBuilder.build()

            for (i in 0 until newBody.size) {
                Timber.i("${newBody.name(i)}  ${newBody.value(i)}")
            }
            newBuilder.post(newBody)
        }

        val response = chain.proceed(newBuilder.build())
        return response
    }
}