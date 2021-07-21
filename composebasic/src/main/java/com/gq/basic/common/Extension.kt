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
 * 秒转hh:mm:ss格式
 * @param secString 秒字符串
 * @return hh小时mm分ss秒
 * String
 */
fun Int.millisecondToFormat(): String {
    val seconds = this / 1000
    var hour = 0
    var min = 0
    var second = 0
    var result = ""
    if (seconds > 60) {   //是否大于零
        min = seconds / 60 //分钟
        second = seconds % 60 //秒
        if (min > 60) {   //存在时
            hour = min / 60
            min %= 60
        }
    }
    if (hour > 0) {
        result = "$hour:"
    }
    if (min > 0) {
        result = "$result$min:"
    } else if (min == 0 && hour > 0) {  //当分为0时,但是时有值,所以要显示,避免出现2时0秒现象
        result = "$result$min:"
    }
    result += second //秒必须出现无论是否大于零
    return result
}