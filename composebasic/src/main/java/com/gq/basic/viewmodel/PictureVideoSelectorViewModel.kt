package com.gq.basic.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.gq.basic.basis.BasicViewModel
import com.gq.basic.common.saveUriFileToAppLocalStorage
import com.gq.basic.compose.PVUris
import com.gq.basic.datasource.PictureVideoPagingSource
import com.gq.basic.viewmodel.repository.PictureVideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PictureVideoSelectorViewModel @Inject constructor(
    application: Application
):BasicViewModel(application) {


    private val pictureVideoRepository: PictureVideoRepository = PictureVideoRepository()
    private var page = 0
    private val limit = 30
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

    fun queryPicUriList(): LiveData<List<PVUris>> {
        val liveData = getMutableLiveData<List<PVUris>>()
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
                        val uris = mutableListOf<PVUris>()
                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(idColumn)
                            val name = cursor.getString(nameColumn)
                            val size = cursor.getInt(sizeColumn)
                            val contentUri: Uri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                            uris.add(PVUris(
                                uri = contentUri,
                                name = name,
                                size = size,
                                type = PVUris.TYPE_PICTURE
                            ))
                        }
                        liveData.postValue(uris)
                    }
            }
        }

        return liveData
    }


    fun queryVideoUriList(): LiveData<List<PVUris>> {
        val liveData = getMutableLiveData<List<PVUris>>()
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
                        val uris = mutableListOf<PVUris>()
                        while (cursor.moveToNext()) {
                            val id = cursor.getLong(idColumn)
                            val name = cursor.getString(nameColumn)
                            val size = cursor.getInt(sizeColumn)
                            val duration = cursor.getInt(durationColumn)
                            val contentUri: Uri = ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                id
                            )
                            uris.add(PVUris(
                                uri = contentUri,
                                name = name,
                                size = size,
                                duration = duration,
                                type = PVUris.TYPE_VIDEO
                            ))
                        }

                        liveData.postValue(uris)
                    }
            }
        }

        return liveData
    }


    val videoAndPicUriListPager = Pager(config = PagingConfig(pageSize = 30, initialLoadSize = 1)) {
        PictureVideoPagingSource(pictureVideoRepository)
    }.flow.cachedIn(viewModelScope)

}