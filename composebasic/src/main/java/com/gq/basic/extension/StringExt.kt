package com.gq.basic.extension

import java.util.regex.Pattern

/**
 * 匹配是否是Log
 */
inline fun String.matchIsLog(): Boolean {
    val index = this.lastIndexOf(".")
    if (index == -1) return false
    val ext = this.substring(index).replace(".", "")
    return Pattern.compile("(log)").matcher(ext).matches()
}