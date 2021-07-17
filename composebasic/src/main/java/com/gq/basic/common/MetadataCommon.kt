package com.gq.basic.common

import android.content.pm.PackageManager
import com.gq.basic.AppContext

/**
 * MetaData
 */
object MetadataCommon {

    fun getMetadataString(key: String): String =
        AppContext
            .application
            .packageManager
            .getApplicationInfo(
                AppContext
                    .application
                    .packageName, PackageManager
                    .GET_META_DATA
            )
            .metaData.getString(key) ?: ""

}