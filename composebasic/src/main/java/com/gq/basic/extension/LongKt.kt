package com.gq.basic.extension

import android.os.Build
import com.gq.basic.common.DateTimeCommon
import timber.log.Timber
import kotlin.system.exitProcess

/**
 * 默认 3天
 */
const val ValidityDay = 60 * 60 * 24 * 5 * 1000

/**
 * 判断测试版的有效期
 * 超过有效期禁止进入app
 * 防止客户卷款跑路
 * 通过build.gradle 配置的
 * productFlavors buildConfigField "long", "BuildTimestamp", "${releaseTime()}"
 */
fun Long.appTestValidity(validityPeriod: Int = ValidityDay) {
    if (this <= 0) return
    val currentTimestamp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeCommon.currentEpochMillis()
    } else {
        DateTimeCommon.currentTimeMillis()
    }
    if (currentTimestamp - this > validityPeriod) {
        Timber.e("App 测试时间配额不足")
        exitProcess(0)
    }
}