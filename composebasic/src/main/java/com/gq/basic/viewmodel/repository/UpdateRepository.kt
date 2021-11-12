package com.gq.basic.viewmodel.repository

import com.gq.basic.AppContext
import com.gq.basic.R
import com.gq.basic.api.UpdateApi
import com.gq.basic.common.DirCommon
import com.gq.basic.data.DownloadApkResult
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
    suspend fun startUpdateApk(url: String): DownloadApkResult<String> =
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

                    return@withContext DownloadApkResult(
                        code = 200,
                        msg = "",
                        data = mutableListOf(path.absolutePath)
                    )
                } ?: let {
                return@withContext DownloadApkResult(
                    code = 400,
                    msg = AppContext.application.getString(R.string.cb_download_fail)
                )
            }
        }


    private fun getPath(): String {
        return DirCommon.getCacheDirFile("apks").absolutePath
    }
}