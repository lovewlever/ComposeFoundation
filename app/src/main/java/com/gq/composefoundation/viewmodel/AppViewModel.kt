package com.gq.composefoundation.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.gq.basic.basis.BasicViewModel
import com.gq.composefoundation.viewmodel.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    application: Application,
    private val appRepository: AppRepository
) : BasicViewModel(application) {

    fun getAppConfig() {
        viewModelScope.launch {
            appRepository.getAppConfig()
        }
    }
}