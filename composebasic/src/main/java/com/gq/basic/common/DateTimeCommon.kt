package com.gq.basic.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeCommon {

    @RequiresApi(Build.VERSION_CODES.O)
    val FormatYMDHMCharacter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    val FormatYMDHM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
    fun currentEpochMillisToString(dateTimeFormatter:DateTimeFormatter) =
        epochMilliToString(dateTimeFormatter, currentEpochMillis())

    @RequiresApi(Build.VERSION_CODES.O)
    fun epochMilliToString(dateTimeFormatter:DateTimeFormatter, epochMilli: Long) =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.of("+8")).format(dateTimeFormatter)

    @RequiresApi(Build.VERSION_CODES.O)
    fun currentEpochMillis() = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()



    val SimpleFormatYMDHMCharacter = SimpleDateFormat("yyyy年MM月dd日HH:mm", Locale.ROOT)
    val SimpleFormatYMDHM = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT)

    fun currentTimeMillis() = System.currentTimeMillis()

    fun timeMillisToString(format: SimpleDateFormat, timeMillis: Long) =
        format.format(Date(timeMillis))
}