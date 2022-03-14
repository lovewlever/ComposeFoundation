package com.gq.basic.common

import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.documentfile.provider.DocumentFile
import com.gq.basic.AppContext
import java.io.*


/**
 * 获取Uri文件扩展名
 */
fun Uri.getFileExtensionName(): String {
    val fileName = DocumentFile.fromSingleUri(AppContext.application, this)?.name
    return fileName?.substringAfterLast(".") ?: ""
}

/**
 * 获取Uri文件名
 */
fun Uri.getFileName(): String {
    val fileName = DocumentFile.fromSingleUri(AppContext.application, this)?.name
    return fileName?.substring(0, fileName.lastIndexOf(".")) ?: ""
}

fun List<Uri>.saveUriFileToAppLocalStorage(
    callback: (MutableList<File>) -> Unit,
    error: (Exception) -> Unit = {}
) {
    val list = mutableListOf<File>()
    this.forEach { i: Uri ->
        AppContext.application.contentResolver.openFileDescriptor(i, "r")?.let {
            saveFileToLocalStorage(i.getFileExtensionName(),it, { f ->
                list.add(f)
                if (list.size == this.size) {
                    callback(list)
                }
            }, error)
        }
    }
}

private fun saveFileToLocalStorage(
    extensionName: String,
    fileDescriptor: ParcelFileDescriptor,
    callback: (File) -> Unit,
    error: (Exception) -> Unit = {}
) {
    var fos: BufferedOutputStream? = null
    var fis: BufferedInputStream? = null
    try {
        val storePath = DirCommon.getCacheDirFile("save_local_file").absolutePath ?: ""
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        val fileName = "${System.currentTimeMillis()}.${extensionName}"
        val file = File(appDir, fileName)

        fos = BufferedOutputStream(FileOutputStream(file))
        fis = BufferedInputStream(FileInputStream(fileDescriptor.fileDescriptor))
        var byteRead: Int
        while (fis.read().apply { byteRead = this } != -1) {
            fos.write(byteRead)
        }
        fos.flush()
        callback(file)
    } catch (e: IOException) {
        e.printStackTrace()
        error(e)
    } finally {
        fis?.close()
        fos?.close()
    }
}