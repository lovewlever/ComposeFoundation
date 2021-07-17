package com.gq.composefoundation

import com.gq.basic.data.ResultEntity
import com.gq.basic.data.RetrofitCall
import retrofit2.http.GET

interface AppApi {

    @GET("/a")
    fun getAppConfig(): RetrofitCall<ResultEntity<String>>
}