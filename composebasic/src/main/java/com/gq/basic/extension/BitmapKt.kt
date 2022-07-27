package com.gq.basic.extension

import android.graphics.Bitmap
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