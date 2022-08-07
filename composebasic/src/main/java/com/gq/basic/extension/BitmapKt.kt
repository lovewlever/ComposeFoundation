package com.gq.basic.extension

import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import com.gq.basic.AppContext
import com.gq.basic.common.DirCommon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.*
import java.nio.ByteBuffer

/**
 * 转ByteArray
 */
fun Bitmap.toByteArray(): ByteArray {
    val byteCount = this.byteCount
    val buf = ByteBuffer.allocate(byteCount)
    this.copyPixelsToBuffer(buf)
    return buf.array()
}


/**
 * 保存图片到相册
 */
suspend fun Bitmap.saveToAlbum(): Pair<Boolean, String> = withContext(Dispatchers.IO) {
    val path = DirCommon.getCacheDirFile("bitmap").absolutePath + File.separator + "bitmap.png"
    var bos: BufferedOutputStream? = null
    var os: OutputStream? = null
    var bis: BufferedInputStream? = null
    try {
        bos = BufferedOutputStream(FileOutputStream(path))
        compress(Bitmap.CompressFormat.PNG, 100, bos)
        bos.flush()
        // 保存到相册
        AppContext.application.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues().also { vc ->
            //vc.put(MediaStore.Images.Media.DATA, path)
            vc.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            vc.put(MediaStore.Images.Media.DISPLAY_NAME, "promote_qr")
        })?.let { uri ->
            os = AppContext.application.contentResolver.openOutputStream(uri)
            bis = BufferedInputStream(FileInputStream(path))
            val arr = ByteArray(4096)
            var len: Int
            while (bis!!.read(arr).also { i -> len = i } != -1) {
                os!!.write(arr, 0, len)
                os!!.flush()
            }
            os!!.flush()
            return@withContext true to "保存成功"
        }
        return@withContext true to "保存失败"
    } catch (e: Exception) {
        Timber.e(e)
        return@withContext true to "保存失败:${e.message}"
    } finally {
        try { bos?.close() } catch (e: Exception) {}
        try { os?.close() } catch (e: Exception) {}
        try { bis?.close() } catch (e: Exception) {}
    }
}