package com.gq.basic.common

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.StringDef
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.core.content.res.ResourcesCompat
import com.gq.basic.AppContext
import com.gq.basic.R
import com.gq.basic.data.AppInfoData
import okhttp3.internal.and
import okio.ByteString.Companion.decodeBase64
import okio.IOException
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Base64.decodeBase64
import org.apache.commons.codec.binary.Base64.encodeBase64String
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Commons {

    object CustomTabs {

        fun startChromeWebTabs(context: Context, url: String?) {
            if (isAppInstalled(context, "com.android.chrome")) {
                //内置启动
                CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build().launchUrl(context, Uri.parse(url))
            } else {
                //启动默认浏览器
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }

        /**
         * 检查app是否已安装
         * Android11中，可以使用此接口。为了功能正常，我们还是需要在manefist中声明<queries>。
         * 获取设备中的所有的应用怎么办？比如我的应用是杀毒软件，需要扫描所有软件。
         * <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>
         */
        private fun isAppInstalled(context: Context, packageName: String?): Boolean {
            val serviceIntent = Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION)
            serviceIntent.setPackage(packageName)
            val resolveInfos = context.packageManager.queryIntentServices(serviceIntent, 0)
            return resolveInfos.isNotEmpty()
        }
    }

    object AppStore {
        const val GooglePlay = "com.android.vending" //GooglePlay
        const val YingYongBao = "com.tencent.android.qqdownloader"	//应用宝
        const val Three60 = "com.qihoo.appstore"	//360手机助手
        const val Baidu = "com.baidu.appsearch"	//百度手机助
        const val Xiaomi = "com.xiaomi.market"	//小米应用商店
        const val Huawei = "com.huawei.appmarket"	//华为应用市场


        fun transferToStore(@StoreType storeType: String, appPackageName: String): Pair<Boolean, String> {
            return try {
                val uri = Uri.parse("market://details?id=$appPackageName")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage(storeType)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                AppContext.application.startActivity(intent)
                true to ""
            } catch (e: Exception) {
                Timber.e(e)
                false to "${e.message}"
            }
        }

        @StringDef(value = [GooglePlay, YingYongBao, Three60, Baidu, Xiaomi, Huawei])
        @Target(AnnotationTarget.VALUE_PARAMETER)
        @Retention(AnnotationRetention.SOURCE)
        annotation class StoreType
    }

    object Version {

        fun getVersionCode(): Long {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                getPackageInfo().longVersionCode
            } else {
                getPackageInfo().versionCode.toLong()
            }
        }


        fun getVersionName(): String =
            getPackageInfo().versionName


        fun getApplicationId(): String =
            AppContext.application.applicationInfo.processName

        private fun getPackageInfo(): PackageInfo =
            AppContext.application.applicationContext
                .packageManager
                .getPackageInfo(AppContext.application.packageName, 0)

    }

    object Device {

        @SuppressLint("MissingPermission")
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
            Settings.System.getString(AppContext.application.contentResolver, Settings.Secure.ANDROID_ID)

        /**
         * 获取设备蓝牙Mac地址
         */
        @SuppressLint("MissingPermission")
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

    object PackageCommon {

        private val packageManager by lazy {
            AppContext.application.packageManager
        }

        /**
         * 获取已安装的app列表
         * <uses-permission
         * android:name="android.permission.QUERY_ALL_PACKAGES"
         * tools:ignore="QueryAllPackagesPermission" />
         */
        fun queryAllPackages(): List<AppInfoData> {
            val appBeanList = mutableListOf<AppInfoData>()
            //val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val packages = packageManager.getInstalledPackages(0)
            packages.forEach { packageInfo ->
                if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用

                }
                packageInfo.versionName
                val bean = AppInfoData(
                    firstInstallTime = packageInfo.firstInstallTime,
                    versionName = packageInfo.versionName,
                    versionCode = packageInfo.versionCode,
                    isSysApp = ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0),
                    icon = packageInfo.applicationInfo.loadIcon(packageManager),
                    appName = packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                    appPackage = packageInfo.applicationInfo.packageName,
                    startActivityClassName = packageInfo.applicationInfo.name
                )
                appBeanList.add(bean)
            }

            return appBeanList
        }

        /**
         * 根据应用包名，查应用的icon图
         */
        private fun loadAppIconByPackageName(packageName: String, listener: (icon: Drawable?) -> Unit) {
            try {
                val packageInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                packageInfo?.applicationInfo?.loadIcon(packageManager)?.let { listener(it) }
            } catch (e: Exception) {
                listener(ResourcesCompat.getDrawable(AppContext.application.resources, R.drawable.ic_cf_video, null))
            }
        }
    }

    object AudioRecord {

        object WavCommon {

            fun pcmToWav(
                pcmFilePath: String,
                sampleRate: Int,
                channels: Int,
                bufferSize: Int,
                deleteOrg: Boolean = true,
            ): String {
                var fis: FileInputStream? = null
                var fos: FileOutputStream? = null
                val totalAudioLen: Long
                val totalDataLen: Long
                val byteRate: Long = (16 * sampleRate * channels / 8).toLong()
                val data = ByteArray(bufferSize)
                try {
                    fis = FileInputStream(pcmFilePath)
                    // 获取路径的 前缀
                    val pathPre = pcmFilePath.substring(0, pcmFilePath.lastIndexOf("/"))
                    // 获取文件名
                    val fileName = pcmFilePath.substring(pcmFilePath.lastIndexOf("/"))
                    val destinationPath = File(pathPre, "${fileName}.wav")
                    fos = FileOutputStream(destinationPath)
                    totalAudioLen = fis.channel.size()
                    totalDataLen = totalAudioLen + 36

                    writeWaveFileHeader(fos, totalAudioLen, totalDataLen,
                        sampleRate, channels, byteRate)
                    while (fis.read(data) != -1) {
                        fos.write(data)
                    }
                    fos.flush()
                    if (deleteOrg) {
                        File(pcmFilePath).delete()
                    }
                    return destinationPath.absolutePath
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        fis?.close()
                    } catch (e: Exception) {
                    }

                    try {
                        fos?.close()
                    } catch (e: Exception) {
                    }
                }
                return ""
            }


            private fun writeWaveFileHeader(
                fos: FileOutputStream,
                totalAudioLen: Long,
                totalDataLen: Long,
                longSampleRate: Int,
                channels: Int,
                byteRate: Long,
            ) {
                val header = ByteArray(44)
                header[0] = 'R'.code.toByte() // RIFF/WAVE header
                header[1] = 'I'.code.toByte()
                header[2] = 'F'.code.toByte()
                header[3] = 'F'.code.toByte()
                header[4] = (totalDataLen and 0xff).toByte()
                header[5] = (((totalDataLen shr 8) and 0xff).toByte())
                header[6] = (((totalDataLen shr 16) and 0xff).toByte())
                header[7] = (((totalDataLen shr 24) and 0xff).toByte())
                header[8] = 'W'.code.toByte() //WAVE
                header[9] = 'A'.code.toByte()
                header[10] = 'V'.code.toByte()
                header[11] = 'E'.code.toByte()
                header[12] = 'f'.code.toByte() // 'fmt ' chunk
                header[13] = 'm'.code.toByte()
                header[14] = 't'.code.toByte()
                header[15] = ' '.code.toByte()
                header[16] = 16 // 4 bytes: size of 'fmt ' chunk
                header[17] = 0
                header[18] = 0
                header[19] = 0
                header[20] = 1 // format = 1
                header[21] = 0
                header[22] = channels.toByte()
                header[23] = 0;
                header[24] = ((longSampleRate and 0xff).toByte())
                header[25] = (((longSampleRate shr 8) and 0xff).toByte())
                header[26] = (((longSampleRate shr 16) and 0xff).toByte())
                header[27] = (((longSampleRate shr 24) and 0xff).toByte())
                header[28] = ((byteRate and 0xff).toByte())
                header[29] = (((byteRate shr 8) and 0xff).toByte())
                header[30] = (((byteRate shr 16) and 0xff).toByte())
                header[31] = (((byteRate shr 24) and 0xff).toByte())
                header[32] = ((2 * 16 / 8).toByte()); // block align
                header[33] = 0
                header[34] = 16 // bits per sample
                header[35] = 0
                header[36] = 'd'.code.toByte() //data
                header[37] = 'a'.code.toByte()
                header[38] = 't'.code.toByte()
                header[39] = 'a'.code.toByte()
                header[40] = ((totalAudioLen and 0xff).toByte())
                header[41] = (((totalAudioLen shr 8) and 0xff).toByte())
                header[42] = (((totalAudioLen shr 16) and 0xff).toByte())
                header[43] = (((totalAudioLen shr 24) and 0xff).toByte())
                fos.write(header, 0, 44)
            }
        }
    }

    object AesCommon {

        private const val IV = "RTdxVng0U2dVUEZndDBvTg=="
        private const val KEY = "OUVZQzlMWDBNMDBiZWFJTg=="
        private const val bm = "UTF-8"
        private val charsetUtf8 = charset("utf-8")

        /**
         * AES 加密
         *
         * @param content  明文
         * @param
         * @return
         */
        fun aesEncrypt(content: String, iv: String = IV, key: String = KEY): String {
            try {
                val iv = String(Base64.decodeBase64(iv),charsetUtf8)
                val pwd = String(Base64.decodeBase64(key),charsetUtf8)
                val zeroIv = IvParameterSpec(iv.toByteArray())
                val key = SecretKeySpec(pwd.toByteArray(), "AES")
                val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv)
                val encryptedData: ByteArray = cipher.doFinal(content.toByteArray(charsetUtf8))
                // return new String(encryptedData,bm);
                return Base64.encodeBase64String(encryptedData)
                //          return byte2HexStr(encryptedData);
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun aesDecrypt(content: String, password: String, iv: String = IV): String? {
            try {
                val byteMi: ByteArray = Base64.decodeBase64(content)
                //          byte[] byteMi=  str2ByteArray(content);
                val zeroIv = IvParameterSpec(iv.toByteArray())
                val key = SecretKeySpec(password.toByteArray(), "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.DECRYPT_MODE, key, zeroIv)
                val decryptedData = cipher.doFinal(byteMi)
                return String(decryptedData, charsetUtf8)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 字节数组转化为大写16进制字符串
         */
        private fun byte2HexStr(b: ByteArray): String {
            val sb = StringBuilder()
            for (i in b.indices) {
                val s = Integer.toHexString(b[i] and 0xFF)
                if (s.length == 1) {
                    sb.append("0")
                }
                sb.append(s.toUpperCase())
            }
            return sb.toString()
        }

        /**
         * 16进制字符串转字节数组
         */
        private fun str2ByteArray(s: String): ByteArray {
            val byteArrayLength = s.length / 2
            val b = ByteArray(byteArrayLength)
            for (i in 0 until byteArrayLength) {
                val b0 = Integer.valueOf(s.substring(i * 2, i * 2 + 2), 16)
                    .toInt().toByte()
                b[i] = b0
            }
            return b
        }
    }
}