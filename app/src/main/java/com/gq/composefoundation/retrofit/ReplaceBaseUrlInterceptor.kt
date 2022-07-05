package com.gq.composefoundation.retrofit

import com.gq.basic.common.DataStoreCommon
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 替换BaseUrl的拦截器
 */
class ReplaceBaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //从request中获取原有的HttpUrl实例oldHttpUrl
        val oldHttpUrl = request.url
        //获取request的创建者builder
        val builder = request.newBuilder()
        val url = DataStoreCommon.getBasicTypeBySP("", "")
        val one = oldHttpUrl.pathSegments.takeIf { it.size > 1 }?.get(1)
        val two = oldHttpUrl.pathSegments.takeIf { it.size > 2 }?.get(2)
        if ((url != "" && one != "getAddressById") && (two != "" && two != "appUpgrade")) {
            val httpUrl = url.toHttpUrl()
            //重建新的HttpUrl，修改需要修改的url部分
            val newFullUrl = oldHttpUrl
                .newBuilder()
                .scheme(httpUrl.scheme)
                .host(httpUrl.host)//更换主机名
                .port(httpUrl.port)
                //.removePathSegment(0)//移除第一个参数
                .build()
            //重建这个request，通过builder.url(newFullUrl).build()
            // 然后返回一个response至此结束修改
            return chain.proceed(builder.url(newFullUrl).build())
        }
        return chain.proceed(request)
    }
}