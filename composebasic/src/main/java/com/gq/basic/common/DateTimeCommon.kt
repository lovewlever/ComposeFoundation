package com.gq.basic.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeCommon {

    val FormatYMDHMCharacter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm")
    val FormatYMDHM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun currentEpochMillisToString(dateTimeFormatter:DateTimeFormatter) =
        epochMilliToString(dateTimeFormatter, currentEpochMillis())

    fun epochMilliToString(dateTimeFormatter:DateTimeFormatter, epochMilli: Long) =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.of("+8")).format(dateTimeFormatter)

    fun currentEpochMillis() = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()
}