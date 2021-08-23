package com.gq.basic.common

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import com.gq.basic.AppContext


object DeviceCommon {

    @Deprecated("不可用")
    fun getDeviceId(): String {
        val telephonyManager = AppContext.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId ?: getAndroidId()
        }
    }


    fun getAndroidId(): String =
        Settings.System.getString(AppContext.application.contentResolver, Secure.ANDROID_ID)
}