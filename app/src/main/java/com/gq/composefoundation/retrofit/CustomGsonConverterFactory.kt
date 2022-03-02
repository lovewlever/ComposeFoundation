package com.gq.composefoundation.retrofit

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.gq.composefoundation.data.ResultEntity
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import java.io.StringReader
import java.lang.reflect.Type

/**
 * @Description:
 * @Author: GQ
 * @Date: 2021/4/5 15:46
 */
class CustomGsonConverterFactory(val gson: Gson): Converter.Factory() {

    companion object {
        fun create(): CustomGsonConverterFactory {
            return create(Gson())
        }
        fun create(gson: Gson?): CustomGsonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return CustomGsonConverterFactory(gson)
        }
    }


    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomResponseBodyConverter(gson,adapter)
    }
}


/**
 * @Description:
 * @Author: GQ
 * @Date: 2021/4/5 15:50
 */
class CustomResponseBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>): Converter<ResponseBody,T> {
    override fun convert(value: ResponseBody): T? {
        val string = value.string()
        Timber.i(string)
        var parseString = JsonParser.parseString(string)
        if (parseString is JsonObject) {
            val data = parseString.get("data")
            // 如果 data是对象 转为数组
            when {
                data?.isJsonObject == true -> {
                    parseString.remove("data")
                    parseString.add("data", JsonArray().apply { add(data) })
                }
                data?.isJsonPrimitive == true -> {
                    val asString = data.asString
                    parseString.remove("data")
                    if (asString != "null" && asString != "\"\"" && asString != "")
                        parseString.add("data", JsonArray().apply { add(asString) })
                }
            }
        } else {
            parseString = JsonParser.parseString(gson.toJson(ResultEntity<T>()))
        }

        val toJson = gson.toJson(parseString)

        return adapter.read(gson.newJsonReader(StringReader(toJson)))
    }
}