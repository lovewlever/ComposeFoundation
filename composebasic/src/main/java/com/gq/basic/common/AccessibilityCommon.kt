package com.gq.basic.common

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import com.gq.basic.AppContext
import timber.log.Timber


object AccessibilityCommon {
    /**
     * 跳转到系统设置：开启辅助服务
     * 鸿蒙系统不能用这个  无障碍功能不会自动启动
     */
    fun openToSystemSetting(cxt: Context) {
        try {
            cxt.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        } catch (e: Throwable) { //若出现异常，则说明该手机设置被厂商篡改了,需要适配
            try {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                cxt.startActivity(intent)
            } catch (e2: Throwable) {
                Timber.e("TAG", "jumpToSetting: " + e2.message)
            }
        }
    }

    /**
     * 检查是否开启无障碍
     */
    fun <T: AccessibilityService> checkAccessibilitySettingsOn(clazz: Class<T>): Boolean {
        var accessibilityEnabled = 0
        val service: String =
            AppContext.application.packageName.toString() + "/" + clazz.canonicalName
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                AppContext.application.applicationContext.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {
            Timber.e(e)
        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                AppContext.application.applicationContext.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        } else {

        }
        return false
    }
}
