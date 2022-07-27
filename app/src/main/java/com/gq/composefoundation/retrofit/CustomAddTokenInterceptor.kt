package com.gq.composefoundation.retrofit

import com.gq.basic.common.DataStoreCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.*
import timber.log.Timber


/**
 * 参数添加TokenAndUid
 */
class CustomAddTokenInterceptor : Interceptor {

    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO + Job()) }
    private var token = ""
    private var sessionId = ""

    init {
        coroutineScope.launch {
            /*DataStoreCommon.getBasicType(Constants.DSKey.Token, "") { token ->
                this@CustomAddTokenInterceptor.token = token
            }*/
        }
        coroutineScope.launch {
            /*DataStoreCommon.getBasicType(Constants.DSKey.SessionId, "") { sessionId ->
                this@CustomAddTokenInterceptor.sessionId = sessionId
            }*/
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newBuilder = request.newBuilder()
        newBuilder
            .header("token", token)
            .header("sessionid", sessionId)

        if (request.method == "GET") {
            val httpUrl: HttpUrl = request.url
                .newBuilder()
                .addQueryParameter("token", token)
                .addQueryParameter("sessionid", sessionId)
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
            bodyBuilder.addEncoded("token", token)
            bodyBuilder.addEncoded("sessionid", sessionId)
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