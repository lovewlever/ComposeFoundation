package com.gq.basic.widget

import android.util.Log
import com.gq.basic.common.DateTimeCommon
import com.gq.basic.common.DirCommon
import com.gq.basic.common.ifNotNullAndEmpty
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * 保存日志文件
 */
class TimberFileTree : Timber.DebugTree() {

    companion object {
        val logFilePath by lazy { DirCommon.getFilesDirFile("timber_log") }
    }
    private val logSuf = ".log"
    private val fileNameTimeFormat by lazy { SimpleDateFormat("yyyy-MM-dd_HH", Locale.ROOT) }
    private val logTimeFormat by lazy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ROOT) }
    private var printWriter: PrintWriter? = null

    init {
        // 当前时间
        val currentDateTime =
            DateTimeCommon.timeMillisToString(fileNameTimeFormat, DateTimeCommon.currentTimeMillis())
        val list = logFilePath.listFiles()
        // 查找有没有当前时间的日志
        list?.filter { f ->
            val b = f.name == currentDateTime + logSuf
            if (!b) {
                //f.delete()
            }
            b
        }?.ifNotNullAndEmpty { it[0] }?.let { f ->
            printWriter = PrintWriter(BufferedWriter(FileWriter(f, true)))
        }
        if (printWriter == null) {
            printWriter = PrintWriter(
                BufferedWriter(
                    FileWriter(
                        File(logFilePath, currentDateTime + logSuf),
                        true
                    )
                )
            )
        }

    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val priorityStr = when (priority) {
            Log.VERBOSE -> "VERBOSE"
            Log.DEBUG -> "DEBUG"
            Log.INFO -> "INFO"
            Log.WARN -> "WARN"
            Log.ERROR -> "ERROR"
            else -> "Fail"
        }
        printWriter
            ?.append("${logTimeFormat.format(System.currentTimeMillis())}:${tag}<%>$priorityStr::${message};${
                t?.stackTraceToString() ?: ""
            }")
            ?.append("\n")
        printWriter?.flush()
    }
}