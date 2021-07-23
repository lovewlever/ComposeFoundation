package com.gq.basic.viewmodel.repository

import com.gq.basic.api.UpdateApi
import com.gq.basic.common.DirCommon
import com.gq.basic.data.ResultEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import javax.inject.Inject

@ViewModelScoped
class UpdateRepository @Inject constructor(
    private val updateApi: UpdateApi,
) {

    /**
     * 下载APK
     */
    suspend fun startUpdateApk(url: String): ResultEntity<String> =
        withContext(Dispatchers.IO) {
            updateApi.downloadUpdateApk(url).execute().body()?.byteStream()
                ?.use { stream: InputStream ->
                    val path = File(getPath(), "update.apk")
                    val bis = BufferedInputStream(stream)
                    val bos = BufferedOutputStream(FileOutputStream(path))
                    var byteRead: Int
                    while (bis.read().apply { byteRead = this } != -1) {
                        bos.write(byteRead)
                    }
                    bos.flush()

                    return@withContext ResultEntity(
                        code = 200,
                        msg = "",
                        data = mutableListOf(path.absolutePath)
                    )
                } ?: let {
                return@withContext ResultEntity(
                    code = 400,
                    msg = "下载失败"
                )
            }
        }


    private fun getPath(): String {
        val path = DirCommon.getCacheDirFile("apks").absolutePath
        return File(path).also {
            if (!it.exists())
                it.mkdirs()
        }.absolutePath
    }
}