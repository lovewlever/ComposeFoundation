package com.gq.composefoundation.custom

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import java.io.IOException


/**
 * 变成不可被继承的
 */
object GsonTypeAdaptersRedefine {
    const val EMPTY = ""

    /**
     * 对于String 类型 的 策略
     */
    val STRING: TypeAdapter<String> = object : TypeAdapter<String>() {
        //进行反序列化
        override fun read(reader: JsonReader): String {
            return try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return ""
                }
                //要进行属性值的判断 若为 空字符串 则返回null 否则返回 本身的值
                val result = reader.nextString()
                if (result.length > 0) result else ""
            } catch (e: Exception) {
                throw JsonSyntaxException(e)
            }
        }

        // 进行序列化
        override fun write(writer: com.google.gson.stream.JsonWriter, value: String?) {
            try {
                if (value == null) {
                    writer.value(EMPTY)
                    return
                }
                writer.value(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 对于int 和 Integer 类型
     * 由于 int 类型 有默认值 0，
     * -- 通常我们无法确定 0 是否具备实际意义
     * 但是 Integer 的类型 null , 我们可以确定的是 -- 无意义的
     *
     * 因此在设计属性的类型是 通常采用 Integer  而不是 int 类型
     *
     * 故 由于 int 的 0  具备 实际意义  -- 不进行转换
     * 而是转换 Integer 类型的 null 值
     */
    val INTEGER: TypeAdapter<Number> = object : TypeAdapter<Number>() {
        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Number {
            println(`in`.peek().toString() + " ----->")
            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                return -1
            }
            if (`in`.peek() == JsonToken.STRING) {
                `in`.nextString()
                return -1
            }
            return `in`.nextInt()
        }

        @Throws(IOException::class)
        override fun write(out: com.google.gson.stream.JsonWriter, value: Number?) {
            if (value == null) {
                out.value(EMPTY)
            } else {
                out.value(value)
            }
        }
    }

    /**
     * 对于double类型的转换
     */
    val DOUBLE: TypeAdapter<Number> = object : TypeAdapter<Number>() {
        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Number {
            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                return -1
            }
            if (`in`.peek() == JsonToken.STRING) {
                `in`.nextString()
                return -1
            }
            return `in`.nextDouble()
        }

        @Throws(IOException::class)
        override fun write(out: com.google.gson.stream.JsonWriter, value: Number?) {
            if (value == null) {
                out.value(EMPTY)
            } else {
                out.value(value)
            }
        }
    }
}
