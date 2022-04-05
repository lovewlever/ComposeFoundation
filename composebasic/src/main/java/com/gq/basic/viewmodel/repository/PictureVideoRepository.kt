package com.gq.basic.viewmodel.repository

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.gq.basic.AppContext
import com.gq.basic.compose.PVUris
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class PictureVideoRepository @Inject constructor() {


    suspend fun queryVideoAndPicUriList(page: Int, limit: Int): List<PVUris> = withContext(Dispatchers.IO) {
        // where条件
        val where =
            "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO} or ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}"
        // 组装查询
        val select = Bundle()
        // 偏移量，也就是从第几条开始查询 page是页码，limit是每页数量，根据逻辑自行修改
        val start = (page - 1) * limit
        select.putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
        select.putInt(ContentResolver.QUERY_ARG_OFFSET, start)
        // 排序方向，倒序
        select.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION,
            ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
        select.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS,
            arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED))
        // where条件
        select.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, where)
        val mediaStoreFilesUri = MediaStore.Files.getContentUri("external")
        var cursor: Cursor? = null
        val uris = mutableListOf<PVUris>()
        try {
            cursor = AppContext.application.contentResolver
                .query(
                    mediaStoreFilesUri,
                    arrayOf(MediaStore.Files.FileColumns._ID,
                        MediaStore.Files.FileColumns.TITLE,
                        MediaStore.Files.FileColumns.SIZE,
                        MediaStore.Files.FileColumns.MEDIA_TYPE),
                    select,
                    null
                )
            cursor?.let {
                val mediaType =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
                //val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val size = cursor.getInt(sizeColumn)
                    val mt = cursor.getInt(mediaType)
                    //val duration = cursor.getInt(durationColumn)
                    val contentUri: Uri = ContentUris.withAppendedId(mediaStoreFilesUri, id)
                    Timber.i(contentUri.path)
                    uris.add(PVUris(
                        uri = contentUri,
                        name = name,
                        size = size,
                        duration = 0,
                        type = if (mt == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) PVUris.TYPE_VIDEO else PVUris.TYPE_PICTURE
                    ))
                }
            }

        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            cursor?.close()
        }
        return@withContext uris
    }
}