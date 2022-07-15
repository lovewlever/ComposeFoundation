package com.gq.basic.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface UpdateApi {

    @Streaming
    @GET
    fun downloadUpdateApk(@Url url: String): Call<ResponseBody>


    /**
     * 查询ip地址
     */
    @GET
    fun queryQueryExternalNetworkIpv4(@Url url: String): Call<ResponseBody>

    /**
     * 查询ipv6地址
     */
    @GET
    fun queryQueryExternalNetworkIpv6(@Url url: String): Call<ResponseBody>
}