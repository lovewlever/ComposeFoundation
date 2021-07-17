package com.gq.basic.common

import com.gq.basic.AppContext
import java.io.File

object DirCommon {

    private val cacheDir by lazy {
        AppContext.application.cacheDir
    }

    private val filesDir by lazy {
        AppContext.application.filesDir
    }

    fun getCacheDirFile(child: String): File =
        File(cacheDir, child).apply {
            if (!exists()) mkdirs()
        }

    fun getFilesDirFile(child: String): File =
        File(filesDir, child).apply {
            if (!exists()) mkdirs()
        }

}