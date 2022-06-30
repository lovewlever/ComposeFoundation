package com.gq.basic.extension

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