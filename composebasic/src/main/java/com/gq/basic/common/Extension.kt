package com.gq.basic.common

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size
import com.gq.basic.AppContext
import java.util.regex.Pattern

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
 * 判断List是否不为null或不为空
 */
inline fun <E,R> Collection<E>?.ifNotNullAndEmpty(block: (MutableList<E>) -> R): R? {
    if (!this.isNullOrEmpty()) {
        return block(this.toMutableList())
    }
    return null
}

/**
 * 匹配手机号
 */
inline fun String.matchPhoneNumber(): Boolean =
    Pattern.compile("^(?:(?:\\+|00)86)?1[3-9]\\d{9}\$").matcher(this).matches()

/**
 * 匹配url
 */
inline fun String.matchUrl(): Boolean =
    Pattern.compile("^(((ht|f)tps?):\\/\\/)?[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?\$").matcher(this).matches()


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