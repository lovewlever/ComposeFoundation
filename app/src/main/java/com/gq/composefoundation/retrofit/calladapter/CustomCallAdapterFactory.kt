package com.gq.composefoundation.retrofit.calladapter

import com.google.gson.JsonParseException
import com.gq.basic.data.RetrofitCall
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CustomCallAdapterFactory: CallAdapter.Factory() {

    companion object {

        fun create(): CustomCallAdapterFactory =
            CustomCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        if (rawType != RetrofitCall::class.java) {
            return null
        }
        val callReturnType = getParameterUpperBound(0, returnType as ParameterizedType)
        return CustomResultCallAdapter(callReturnType)
    }
}


class CustomResultCallAdapter(private val responseType: Type): CallAdapter<Any,RetrofitCall<*>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Any>): RetrofitCall<*> {
        Timber.d("CustomResultCallAdapter#adapt")
        val body = call.execute().body() ?: throw JsonParseException("JSON解析失败")
        return RetrofitCall(body)
    }

}