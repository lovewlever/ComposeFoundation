package com.gq.basic.extension

import com.gq.basic.common.GsonCommon

fun <K, V> Map<K,V>.toGsonStr() =
    GsonCommon.gson.toJson(this)
