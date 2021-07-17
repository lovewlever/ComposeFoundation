package com.gq.composefoundation.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okio.*

/**
 * 进度拦截器
 */
class ProgressInterceptor(private val progressListener: ProgressListener):
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse: Response = chain.proceed(chain.request())
        return originalResponse.newBuilder()
            .body(
                ProgressResponseBody(chain.request().url.toUrl().toString(),
                originalResponse.body, progressListener)
            )
            .build()
    }
}


interface ProgressListener {
    fun update(url: String?, bytesRead: Long, contentLength: Long, done: Boolean)
}

/**
 * 进度
 */
class ProgressResponseBody(
    private var url: String?,
    private var responseBody: ResponseBody?,
    private var progressListener: ProgressListener
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody?.contentType()
    }

    override fun contentLength(): Long {
        return responseBody?.contentLength() ?: 0L
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = responseBody?.source()?.let { source(it).buffer() }
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressListener.update(url,
                    totalBytesRead,
                    contentLength(),
                    bytesRead == -1L)
                return bytesRead
            }
        }
    }
}