package com.gq.composefoundation.api

import com.gq.basic.data.DownloadApkResult
import com.gq.composefoundation.retrofit.calladapter.RetrofitCall
import retrofit2.http.GET

interface AppApi {

    @GET("/a")
    fun getAppConfig(): RetrofitCall<DownloadApkResult<String>>
}