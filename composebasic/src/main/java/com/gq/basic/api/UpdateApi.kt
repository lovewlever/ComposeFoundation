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
}