package com.gq.composefoundation.api

import com.gq.composefoundation.data.ResultEntity
import com.gq.composefoundation.retrofit.RetrofitCall
import retrofit2.http.GET

interface AppApi {

    @GET("user/query-users")
    fun getAppConfig(): RetrofitCall<ResultEntity<String>>
}