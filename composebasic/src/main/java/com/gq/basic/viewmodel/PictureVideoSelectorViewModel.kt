package com.gq.basic.viewmodel

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.gq.basic.basis.BasicViewModel
import com.gq.basic.compose.PVUris
import com.gq.basic.compose.PictureVideoSelectorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PictureVideoSelectorViewModel @Inject constructor(application: Application) :
    BasicViewModel(application) {

    val urisState = mutableStateListOf<PVUris>()

    private val imageProjection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE
    )
    private val imageSortOrder = "${MediaStore.Images.Media._ID} DESC"

    private val videoProjection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE
    )
    private val videoSortOrder = "${MediaStore.Video.Media._ID} DESC"

    fun queryPicUriList() {
        urisState.clear()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getApplication<Application>().contentResolver
                    .query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        imageProjection,
                        null,
                        null,
                        imageSortOrder
                    )
                    ?.use { cursor ->
                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                        val nameColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(idColumn)
                            val name = cursor.getString(nameColumn)
                            val size = cursor.getInt(sizeColumn)
                            val contentUri: Uri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                            urisState.add(PVUris(
                                uri = contentUri,
                                name = name,
                                size = size,
                                type = PVUris.TYPE_PICTURE
                            ))
                        }
                    }
            }
        }
    }


    fun queryVideoUriList() {
        urisState.clear()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getApplication<Application>().contentResolver
                    .query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        videoProjection,
                        null,
                        null,
                        videoSortOrder
                    )
                    ?.use { cursor ->
                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                        val nameColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                        val durationColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(idColumn)
                            val name = cursor.getString(nameColumn)
                            val size = cursor.getInt(sizeColumn)
                            val duration = cursor.getInt(durationColumn)
                            val contentUri: Uri = ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                            urisState.add(PVUris(
                                uri = contentUri,
                                name = name,
                                size = size,
                                duration = duration,
                                type = PVUris.TYPE_VIDEO
                            ))
                        }
                    }
            }
        }
    }
}