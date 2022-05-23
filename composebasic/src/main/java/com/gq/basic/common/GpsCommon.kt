package com.gq.basic.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import com.gq.basic.AppContext
import timber.log.Timber

/**
 * Gsp
 */
object GpsCommon {

    private val locationManager by lazy {
        AppContext.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    var onLocationChange: (Location) -> Unit = {}
    private val locationListener by lazy {
        object :LocationListener {
            override fun onLocationChanged(location: Location) {
                Timber.i("${location.latitude} -- ${location.longitude}")
                onLocationChange(location)
            }

            @SuppressLint("MissingPermission")
            override fun onProviderEnabled(provider: String) {
                super.onProviderEnabled(provider)
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    ?.let { onLocationChange(it) }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun openGps(onLocationChange: (Location) -> Unit): Boolean {
        return if (isOpenGps()) {
            this.onLocationChange = onLocationChange
            locationManager.getBestProvider(Criteria().also { criteria ->
                criteria.accuracy = Criteria.ACCURACY_FINE
                // 设置是否要求速度
                criteria.isSpeedRequired = false
                // 设置是否允许运营商收费
                criteria.isCostAllowed = true
                // 设置是否需要方位信息
                criteria.isBearingRequired = false
                // 设置是否需要海拔信息
                criteria.isAltitudeRequired = false
                // 设置对电源的需求
                criteria.powerRequirement = Criteria.POWER_HIGH
            }, true)?.let { bestProvider ->
                locationManager.requestLocationUpdates(bestProvider,
                    0, 0F, locationListener)
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    ?.let { onLocationChange(it) }
            }

            true
        } else {
            false
        }
    }

    fun release() {
        locationManager.removeUpdates(locationListener)
    }


    fun isOpenGps() =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    fun openGpsSettingActivity() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AppContext.application.startActivity(intent)
    }
}