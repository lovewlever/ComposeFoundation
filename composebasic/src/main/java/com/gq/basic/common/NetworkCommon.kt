package com.gq.basic.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.telephony.TelephonyManager
import com.gq.basic.AppContext
import timber.log.Timber

object NetworkCommon {

    var registeredCurrentNetworkType: String = "UNKNOWN"
        private set(value) { field = value }

    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            @SuppressLint("MissingPermission")
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                Timber.d("ConnectivityManager onCapabilitiesChanged ");
                // 一般在此处获取网络类型然后判断网络类型，就知道时哪个网络可以用connected
                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {//WIFI
                        registeredCurrentNetworkType = "WIFI"
                    }
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {//移动数据
                        val tm =
                            AppContext.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        val type = tm.dataNetworkType
                        if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                            registeredCurrentNetworkType = "UNKNOWN"
                        }
                        registeredCurrentNetworkType = when (type) {
                            TelephonyManager.NETWORK_TYPE_GPRS,
                            TelephonyManager.NETWORK_TYPE_CDMA,
                            TelephonyManager.NETWORK_TYPE_EDGE,
                            TelephonyManager.NETWORK_TYPE_1xRTT,
                            TelephonyManager.NETWORK_TYPE_IDEN -> { "2G" }
                            TelephonyManager.NETWORK_TYPE_EVDO_A,
                            TelephonyManager.NETWORK_TYPE_UMTS,
                            TelephonyManager.NETWORK_TYPE_EVDO_0,
                            TelephonyManager.NETWORK_TYPE_HSDPA,
                            TelephonyManager.NETWORK_TYPE_HSUPA,
                            TelephonyManager.NETWORK_TYPE_HSPA,
                            TelephonyManager.NETWORK_TYPE_EVDO_B,
                            TelephonyManager.NETWORK_TYPE_EHRPD,
                            TelephonyManager.NETWORK_TYPE_HSPAP -> { "3G" }
                            TelephonyManager.NETWORK_TYPE_LTE -> { "LTE" }
                            TelephonyManager.NETWORK_TYPE_NR -> { "5G" }
                            else -> { "UNKNOWN" }
                        }
                    }
                    else -> {
                        registeredCurrentNetworkType = "UNKNOWN"
                    }
                }
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)

            }

        }
    }

    private val connectivityManager by lazy {
        AppContext.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * 检查网络连接和网络类型
     */
    fun isNetworkAvailableAndType(): Pair<Boolean, String> {
        val nw = connectivityManager.activeNetwork ?: return false to ""
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false to ""
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true to "WIFI"
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                val tm =
                    AppContext.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val type = tm.dataNetworkType
                if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                    return true to "UNKNOWN"
                }
                when (type) {
                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_1xRTT,
                    TelephonyManager.NETWORK_TYPE_IDEN,
                    -> {
                        true to "2G"
                    }
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_EHRPD,
                    TelephonyManager.NETWORK_TYPE_HSPAP,
                    -> {
                        true to "3G"
                    }
                    TelephonyManager.NETWORK_TYPE_LTE -> {
                        true to "LTE"
                    }
                    TelephonyManager.NETWORK_TYPE_NR -> {
                        true to "5G"
                    }
                    else -> {
                        return true to "UNKNOWN"
                    }
                }
                true to "UNKNOWN"
            }
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true to "Ethernet"
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true to "Bluetooth"
            else -> false to "UNKNOWN"
        }
    }

    /**
     * 检查网络连接
     */
    fun isNetworkAvailable(): Boolean {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->  true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    fun registerNetworkCallback() {
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build(), networkCallback)
    }

    fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}