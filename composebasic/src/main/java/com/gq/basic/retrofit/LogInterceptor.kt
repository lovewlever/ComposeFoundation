package com.gq.basic.retrofit

import com.google.gson.JsonObject
import com.gq.basic.common.GsonCommon
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.IOException
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

/**
 * 日志拦截器
 */
class LogInterceptor : Interceptor {

    private val sb by lazy { StringBuilder() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        printRequestMessage(request)
        val response: Response = chain.proceed(request)
        if (!response.isSuccessful) {
            val code = response.code
            val requestBody = response.body
            val contentType = requestBody?.contentType()
            val str = requestBody?.string()
            Timber.e("<-- HTTP: ${response.code} \n${str}")
            return response.newBuilder().body(str?.toResponseBody(contentType)).code(code).build()
        }
        printResponseMessage(response)
        return response
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private fun printRequestMessage(request: Request) {
        sb.clear()
        val requestBody: RequestBody? = request.body
        val headers = request.headers
        val url = request.url.toUrl()
        val method = request.method
        Timber.e("--> URL: $url")
        Timber.e("--> URL-HOST: ${url.host}")
        Timber.e("--> URL-PORT: ${url.port}")
        Timber.e("--> URL-PATH: ${url.path}")
        Timber.e("--> URL-PROTOCOL: ${url.protocol}")
        Timber.e("--> Method: $method (${requestBody?.contentLength()}-byte body)")
        headers.toList().forEach { h -> sb.append("\n").append(h.first).append(": ").append(h.second) }
        Timber.e("--> Headers: $sb")
        requestBody?.contentType()?.let {
            if (headers["Content-Type"] == null) {
                Timber.e("--> Content-Type: $it")
            }
        }
        if (requestBody?.contentLength() != -1L) {
            if (headers["Content-Length"] == null) {
                Timber.e("--> Content-Length: ${requestBody?.contentLength()}")
            }
        }

        try {
            val params = if (method == "GET") {
                url.query
            } else {
                val bufferedSink = okio.Buffer()
                requestBody?.writeTo(bufferedSink)
                var charset: Charset? = requestBody?.contentType()?.charset()
                charset = charset ?: Charset.forName("utf-8")
                charset?.let { bufferedSink.readString(it) }
            }
            Timber.e("--> Params: ?$params")
            params?.split("&")?.let { list ->
                val joStr = JsonObject().also { jo ->
                    list.forEachIndexed { index, s ->
                        val sp = s.split("=")
                        if (sp.size > 1) {
                            jo.addProperty(sp[0], sp[1])
                        }
                    }
                }.toString()
                Timber.e("--> Params-Json: $joStr")
            }
        } catch (e: IOException) {
            Timber.e("--> Exception: ${e.message}")
            Timber.e(e)
        }
    }

    /**
     * 打印返回消息
     *
     * @param response 返回的对象
     */
    private fun printResponseMessage(response: Response) {
        val responseBody: ResponseBody? = response.body
        val source = responseBody?.source()
        try {
            source?.request(Long.MAX_VALUE) // Buffer the entire body.
        } catch (e: IOException) {
            Timber.e("<-- Exception: ${e.message}")
            Timber.e(e)
        }
        val buffer: okio.Buffer? = source?.buffer()
        val contentType: MediaType? = responseBody?.contentType()
        val charset = contentType?.charset() ?: UTF_8
        Timber.e("<-- Content-Type: $contentType")

        val result: String? = buffer?.clone()?.readString(charset)
        //Timber.e("<-- Result: ${GsonCommon.gson.toJson(result)?.replace("\\\"", "\"")}")
        Timber.e("<-- Result: $result")
        Timber.e("<-- HTTP: ${response.code}")
        Timber.e("<-- END: (${buffer?.size}-byte body)\n")
    }
}