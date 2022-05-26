package com.gq.basic.data

import android.graphics.drawable.Drawable


class AppInfoData(
    var icon: Drawable?,
    var appName: String = "",
    var appPackage: String = "",
    var isSysApp: Boolean = false,
    var versionName: String = "",
    var versionCode: Int = -1,
    var firstInstallTime: Long = 0L,
    var startActivityClassName: String = ""
)