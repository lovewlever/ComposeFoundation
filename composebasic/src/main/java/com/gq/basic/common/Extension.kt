package com.gq.basic.common

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import com.gq.basic.AppContext

/**
 * 获取视频第一帧
 */
fun Uri.getVideoFirstFrame(): Bitmap? {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(AppContext.application, this)
    return mediaMetadataRetriever.frameAtTime
}

/**
 * 获取文件缩略图
 */
fun Uri.loadVideoThumbnail(): Bitmap? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return AppContext.application.contentResolver.loadThumbnail(this, Size(640, 480), null)
    } else {
        return getVideoFirstFrame()
    }
}

/**
 * Int转双位时间
 * 00
 */
fun Int.toDoublePositionTime(): String {
    val len = this.toString().length
    return if (len <= 1) "0${this}" else this.toString()
}

/**
 * 毫秒转hh:mm:ss格式
 * String
 */
fun Int.millisecondToHms(): String {
    var seconds = this / 1000
    val minute: Int
    // 小于60秒
    if (seconds < 60) {
        return "00:${seconds.toDoublePositionTime()}"
    }
    // 小于60分钟
    if (seconds / 60 < 60) {
        minute = seconds / 60
        return "${minute.toDoublePositionTime()}:${(seconds % 60).toDoublePositionTime()}"
    }

    val hour: Int = seconds / 60 / 60
    minute = seconds / 60 % 60
    seconds = (seconds - (hour * 60 * 60)) % 60

    return "${hour.toDoublePositionTime()}:${minute.toDoublePositionTime()}:${seconds.toDoublePositionTime()}"
}