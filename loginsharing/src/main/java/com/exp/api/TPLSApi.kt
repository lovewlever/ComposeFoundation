package com.exp.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface TPLSApi {

    @GET
    fun accessToken(
        @Url url: String
    ): Call<ResponseBody>

    /**
     * 获取用户个人信息（UnionID 机制）
     */
    @GET
    fun userWxInfo(
        @Url url: String
    ): Call<ResponseBody>
}