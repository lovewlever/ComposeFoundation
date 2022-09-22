package com.gq.basic.extension

import com.google.gson.Gson
import com.gq.basic.common.GsonCommon

/**
 * 判断List是否不为null或不为空
 */
inline fun <E,R> Collection<E>?.ifNotNullAndEmpty(block: (MutableList<E>) -> R): R? {
    if (!this.isNullOrEmpty()) {
        return block(this.toMutableList())
    }
    return null
}

/**
 * 判断List是否不为null或不为空
 */
fun <E> Collection<E>.toGsonArrayStr(gson: Gson = GsonCommon.gson): String {
    return gson.toJson(this)
}