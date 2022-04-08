package com.gq.basic.common

import android.content.pm.PackageInfo
import com.gq.basic.AppContext

@Deprecated("Commons.Version")
object VersionCommon {
    @Deprecated("Commons.Version.getVersionCode")
    fun getVersionCode(): Long {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            getPackageInfo().longVersionCode
        } else {
            getPackageInfo().versionCode.toLong()
        }
    }

    @Deprecated("Commons.Version.getVersionName")
    fun getVersionName(): String =
        getPackageInfo().versionName

    @Deprecated("Commons.Version.getApplicationId")
    fun getApplicationId(): String =
        AppContext.application.applicationInfo.processName

    private fun getPackageInfo(): PackageInfo =
        AppContext.application.applicationContext
            .packageManager
            .getPackageInfo(AppContext.application.packageName, 0)

}