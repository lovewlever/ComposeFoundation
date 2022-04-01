package com.gq.basic.common

import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import com.gq.basic.AppContext
import timber.log.Timber
import java.util.*


object DeviceCommon {

    @Deprecated("不可用")
    fun getDeviceId(): String {
        val telephonyManager =
            AppContext.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId ?: getAndroidId()
        }
    }


    fun getAndroidId(): String =
        Settings.System.getString(AppContext.application.contentResolver, Secure.ANDROID_ID)

    /**
     * 获取设备蓝牙Mac地址
     */
    fun getBluetoothMacAddress(): String? {
        val bluetoothManager =
            AppContext.application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        return bluetoothAdapter.address
    }


    /**
     * Pseudo-Unique ID
     */
    fun getUniquePseudoId(): String {
        val dev = "35" +
                Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                Build.USER.length % 10

        var serial = "serial"
        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null)?.toString() ?: serial
        } catch (e: Exception) {
            Timber.e(e)
        }
        return UUID(dev.hashCode().toLong(), serial.hashCode().toLong()).toString().replace("-", "")
    }
}