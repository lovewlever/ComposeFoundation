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

    /**
     * 清空目录
     * 包括当前目录
     */
    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            //递归删除目录中的子目录下
            children.forEachIndexed { index, s ->
                val success = deleteDir(File(dir, children[index]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }

}