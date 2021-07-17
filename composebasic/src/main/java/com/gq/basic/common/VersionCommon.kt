package com.gq.basic.common

import android.content.pm.PackageInfo
import com.gq.basic.AppContext

object VersionCommon {

    fun getVersionCode(): Long {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            getPackageInfo().longVersionCode
        } else {
            getPackageInfo().versionCode.toLong()
        }
    }


    fun getVersionName(): String =
        getPackageInfo().versionName


    private fun getPackageInfo(): PackageInfo =
        AppContext.application.applicationContext
            .packageManager
            .getPackageInfo(AppContext.application.packageName, 0)

}