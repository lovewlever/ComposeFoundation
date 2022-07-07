package com.gq.composefoundation.viewmodel.repository

import com.gq.composefoundation.api.AppApi
import com.gq.composefoundation.data.ResultEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ViewModelScoped
class AppRepository @Inject constructor(private val appApi: AppApi) {

    suspend fun getAppConfig() = withContext(Dispatchers.IO) {
        try {
            val resultEntity = appApi.getAppConfig().obtain
            resultEntity
        } catch (e: Exception) {
            Timber.e(e)
            ResultEntity(msg = "${e.message}")
        }
    }
}