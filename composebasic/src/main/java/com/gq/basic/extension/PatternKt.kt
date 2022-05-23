package com.gq.basic.extension

import java.util.regex.Pattern

/**
 * 匹配是否是图片
 */
inline fun String.matchIsImage(): Boolean {
    val index = this.lastIndexOf(".")
    if (index == -1) return false
    val ext = this.substring(index).replace(".", "")
    return Pattern.compile("(gif|png|jpg|jpeg|webp|svg|psd|bmp|tif|jfif)").matcher(ext).matches()
}

/**
 * 匹配是否是音频文件
 */
inline fun String.matchIsAudio(): Boolean {
    val index = this.lastIndexOf(".")
    if (index == -1) return false
    val ext = this.substring(index).replace(".", "").lowercase()
    return Pattern.compile("(mp3|m4a|wav|amr|awb|wma|ogg|flac|pcm)").matcher(ext).matches()
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
