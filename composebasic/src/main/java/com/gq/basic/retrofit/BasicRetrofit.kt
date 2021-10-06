package com.gq.basic.retrofit

import com.gq.basic.AppContext
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * @Description:
 * @Author: GQ
 * @Date: 2021/4/2 16:42
 */
@Singleton
class BasicRetrofit @Inject constructor() {

    private val cookieStore = HashMap<String, List<Cookie>>()
    private lateinit var retrofitInstance: Retrofit

    fun <T : Any> retrofit(service: KClass<T>): T {
        return retrofitInstance.create(service.java)
    }

    fun initialization(
        baseUrl: String,
        cache: Boolean = false,
        interceptor: MutableList<Interceptor>? = null,
        interceptorNetwork: MutableList<Interceptor>? = null,
        factoryCallAdapter: MutableList<CallAdapter.Factory>? = null,
        factoryConverter: MutableList<Converter.Factory>? = null
    ) {
        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
                .proxy(Proxy.NO_PROXY)
                .cookieJar(saveCookie())
                .callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(printResponse())
                .apply {
                    interceptor?.forEach {
                        addInterceptor(it)
                    }
                    interceptorNetwork?.forEach {
                        addNetworkInterceptor(it)
                    }
                    if (cache) {
                        val cacheDir = AppContext.application.cacheDir
                        cache(Cache(cacheDir, (10 * 1024 * 1024).toLong()))
                    }
                }
                .retryOnConnectionFailure(true)//允许重试
                .build()

        retrofitInstance = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .apply {
                factoryCallAdapter?.forEach {
                    addCallAdapterFactory(it)
                }
                factoryConverter?.forEach {
                    addConverterFactory(it)
                }
            }
            .build()
    }


    /**
     * 打印响应信息
     * @return
     */
    private fun printResponse(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                try {
                    //val text = URLDecoder.decode(message, "utf-8")
                    Timber.i(message)
                } catch (e: UnsupportedEncodingException) {
                    Timber.e(e)
                }
            }

        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    /**
     * 保存Cookie
     * @return
     */
    private fun saveCookie(): CookieJar =
        object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host] = cookies
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies = cookieStore[url.host]
                return cookies ?: ArrayList()
            }
        }

}