package com.gq.basic.extension

import android.content.pm.ApplicationInfo

/**
 * 判断当前应用是否是debug状态
 */
fun ApplicationInfo.isApkInDebug(): Boolean {
    return try {
        flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: Exception) {
        false
    }
}