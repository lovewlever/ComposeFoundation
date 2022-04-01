package com.gq.basic.common

import okio.IOException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object AudioRecordCommon {

    object WavCommon {

        fun pcmToWav(
            pcmFilePath: String,
            sampleRate: Int,
            channels: Int,
            bufferSize: Int,
            deleteOrg: Boolean = true,
        ): String {
            var fis: FileInputStream? = null
            var fos: FileOutputStream? = null
            val totalAudioLen: Long
            val totalDataLen: Long
            val byteRate: Long = (16 * sampleRate * channels / 8).toLong()
            val data = ByteArray(bufferSize)
            try {
                fis = FileInputStream(pcmFilePath)
                // 获取路径的 前缀
                val pathPre = pcmFilePath.substring(0, pcmFilePath.lastIndexOf("/"))
                // 获取文件名
                val fileName = pcmFilePath.substring(pcmFilePath.lastIndexOf("/"))
                val destinationPath = File(pathPre, "${fileName}.wav")
                fos = FileOutputStream(destinationPath)
                totalAudioLen = fis.channel.size()
                totalDataLen = totalAudioLen + 36

                writeWaveFileHeader(fos, totalAudioLen, totalDataLen,
                    sampleRate, channels, byteRate)
                while (fis.read(data) != -1) {
                    fos.write(data)
                }
                fos.flush()
                if (deleteOrg) {
                    File(pcmFilePath).delete()
                }
                return destinationPath.absolutePath
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    fis?.close()
                } catch (e: Exception) {
                }

                try {
                    fos?.close()
                } catch (e: Exception) {
                }
            }
            return ""
        }


        private fun writeWaveFileHeader(
            fos: FileOutputStream,
            totalAudioLen: Long,
            totalDataLen: Long,
            longSampleRate: Int,
            channels: Int,
            byteRate: Long,
        ) {
            val header = ByteArray(44)
            header[0] = 'R'.code.toByte() // RIFF/WAVE header
            header[1] = 'I'.code.toByte()
            header[2] = 'F'.code.toByte()
            header[3] = 'F'.code.toByte()
            header[4] = (totalDataLen and 0xff).toByte()
            header[5] = (((totalDataLen shr 8) and 0xff).toByte())
            header[6] = (((totalDataLen shr 16) and 0xff).toByte())
            header[7] = (((totalDataLen shr 24) and 0xff).toByte())
            header[8] = 'W'.code.toByte() //WAVE
            header[9] = 'A'.code.toByte()
            header[10] = 'V'.code.toByte()
            header[11] = 'E'.code.toByte()
            header[12] = 'f'.code.toByte() // 'fmt ' chunk
            header[13] = 'm'.code.toByte()
            header[14] = 't'.code.toByte()
            header[15] = ' '.code.toByte()
            header[16] = 16 // 4 bytes: size of 'fmt ' chunk
            header[17] = 0
            header[18] = 0
            header[19] = 0
            header[20] = 1 // format = 1
            header[21] = 0
            header[22] = channels.toByte()
            header[23] = 0;
            header[24] = ((longSampleRate and 0xff).toByte())
            header[25] = (((longSampleRate shr 8) and 0xff).toByte())
            header[26] = (((longSampleRate shr 16) and 0xff).toByte())
            header[27] = (((longSampleRate shr 24) and 0xff).toByte())
            header[28] = ((byteRate and 0xff).toByte())
            header[29] = (((byteRate shr 8) and 0xff).toByte())
            header[30] = (((byteRate shr 16) and 0xff).toByte())
            header[31] = (((byteRate shr 24) and 0xff).toByte())
            header[32] = ((2 * 16 / 8).toByte()); // block align
            header[33] = 0
            header[34] = 16 // bits per sample
            header[35] = 0
            header[36] = 'd'.code.toByte() //data
            header[37] = 'a'.code.toByte()
            header[38] = 't'.code.toByte()
            header[39] = 'a'.code.toByte()
            header[40] = ((totalAudioLen and 0xff).toByte())
            header[41] = (((totalAudioLen shr 8) and 0xff).toByte())
            header[42] = (((totalAudioLen shr 16) and 0xff).toByte())
            header[43] = (((totalAudioLen shr 24) and 0xff).toByte())
            fos.write(header, 0, 44)
        }
    }
}

