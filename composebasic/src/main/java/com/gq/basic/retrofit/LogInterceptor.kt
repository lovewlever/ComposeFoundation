package com.gq.basic.retrofit

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.gq.basic.common.GsonCommon
import com.gq.basic.extension.toEntityDataByGson
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

/**
 * 日志拦截器
 */
class LogInterceptor : Interceptor {

    private val sb by lazy { StringBuilder() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val headers = request.headers
        val url = request.url.toUrl()
        val method = request.method
        val urlPath = url.path
        // 打印请求值
        printRequestMessage(
            url = url,
            method = method,
            headers = headers,
            urlPath = urlPath,
            request
        )
        val response: Response = chain.proceed(request)
        if (!response.isSuccessful) {
            val code = response.code
            val requestBody = response.body
            val contentType = requestBody?.contentType()
            val str = requestBody?.string()
            Timber.e("$urlPath <-- HTTP: ${response.code} \n${str}")
            return response.newBuilder().body(str?.toResponseBody(contentType)).code(code).build()
        }
        // 打印返回值
        printResponseMessage(urlPath = urlPath, response)
        return response
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private fun printRequestMessage(
        url: URL,
        method: String,
        headers: Headers,
        urlPath: String,
        request: Request
    ) {
        sb.clear()
        val requestBody: RequestBody? = request.body
        Timber.e("$urlPath --> URL: $url")
        Timber.e("$urlPath --> URL-HOST: ${url.host}")
        Timber.e("$urlPath --> URL-PORT: ${url.port}")
        Timber.e("$urlPath --> URL-PATH: $urlPath")
        Timber.e("$urlPath --> URL-PROTOCOL: ${url.protocol}")
        Timber.e("$urlPath --> Method: $method (${requestBody?.contentLength()}-byte body)")
        headers.toList()
            .forEach { h -> sb.append("\n").append(h.first).append(": ").append(h.second) }
        Timber.e("$urlPath --> Headers: $sb")
        requestBody?.contentType()?.let {
            if (headers["Content-Type"] == null) {
                Timber.e("$urlPath --> Content-Type: $it")
            }
        }
        if (requestBody?.contentLength() != -1L) {
            if (headers["Content-Length"] == null) {
                Timber.e("$urlPath --> Content-Length: ${requestBody?.contentLength()}")
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

            val jsonEl = JsonParser.parseString(params)
            val isJson = jsonEl.isJsonArray || jsonEl.isJsonObject


            if (isJson) {
                sb.clear()
                // 生成Query参数
                val queryParams = params?.toEntityDataByGson<JsonObject>()?.let { jsonObj ->
                    jsonObj.keySet().forEach { key ->
                        sb.append(key).append("=").append(jsonObj.get(key).asString).append("&")
                    }
                    if (sb.isNotBlank()) {
                        val index = sb.lastIndexOf("&")
                        if (index > 0) sb.deleteCharAt(index)
                    }
                    sb.toString()
                }
                Timber.e("$urlPath --> Params-Query: ?$queryParams")
                Timber.e("$urlPath --> Params-Json: $params")
            } else {
                Timber.e("$urlPath --> Params-Query: ?$params")
                params?.split("&")?.let { list ->
                    val joStr = JsonObject().also { jo ->
                        list.forEachIndexed { index, s ->
                            val sp = s.split("=")
                            if (sp.size > 1) {
                                jo.addProperty(sp[0], sp[1])
                            }
                        }
                    }.toString()
                    Timber.e("$urlPath --> Params-Json: $joStr")
                }
            }
        } catch (e: IOException) {
            Timber.e("$urlPath --> Exception: ${e.message}")
            Timber.e(e)
        }
    }

    /**
     * 打印返回消息
     *
     * @param response 返回的对象
     */
    private fun printResponseMessage(urlPath: String, response: Response) {
        val responseBody: ResponseBody? = response.body
        val source = responseBody?.source()
        try {
            source?.request(Long.MAX_VALUE) // Buffer the entire body.
        } catch (e: IOException) {
            Timber.e("$urlPath <-- Exception: ${e.message}")
            Timber.e(e)
        }
        val buffer: okio.Buffer? = source?.buffer()
        val contentType: MediaType? = responseBody?.contentType()
        val charset = contentType?.charset() ?: UTF_8
        Timber.e("$urlPath <-- Content-Type: $contentType")

        val result: String? = buffer?.clone()?.readString(charset)
        //Timber.e("<-- Result: ${GsonCommon.gson.toJson(result)?.replace("\\\"", "\"")}")
        Timber.e("$urlPath <-- Result: $result")
        Timber.e("$urlPath <-- HTTP: ${response.code}")
        Timber.e("$urlPath <-- END: (${buffer?.size}-byte body)\n")
    }
}