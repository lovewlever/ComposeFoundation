package com.gq.basic.extension

import android.os.Parcelable
import com.google.gson.Gson
import com.gq.basic.common.GsonCommon

inline fun Parcelable.toGsonStr(gson: Gson = GsonCommon.gson): String =
    gson.toJson(this)