package com.gq.composefoundation.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import timber.log.Timber
import java.nio.charset.Charset


/**
 * 未登录状态拦截
 */
class LoginFailInterceptor : Interceptor {

    private var curRequestUrl: String = ""
    private var requestNumber = 1
    companion object {
        var loginOutCallback: () -> Unit = {}
    }

    private val UTF8 = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val encodedPath = request.url.encodedPath
        Timber.i(encodedPath)
        val resp = chain.proceed(request)
        if (resp.isSuccessful) {
            val source = resp.body!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            val contentType = resp.body!!.contentType()
            val charset: Charset = if (contentType != null)
                contentType.charset(UTF8)!!
            else
                UTF8
            val json = buffer.clone().readString(charset)
            val jsObj= JSONObject(json)
            if (jsObj.optInt("code") == 10000) {
                if (encodedPath != curRequestUrl) {
                    loginOutCallback()
                    requestNumber = 1
                } else {
                    if (requestNumber == 0) {
                        loginOutCallback()
                        requestNumber = 1
                    } else {
                        requestNumber --
                    }
                }
                curRequestUrl = encodedPath
            }
        }
        return resp
    }
}