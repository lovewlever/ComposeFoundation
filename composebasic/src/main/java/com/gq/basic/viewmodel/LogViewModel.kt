package com.gq.basic.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gq.basic.basis.BasicViewModel
import com.gq.basic.extension.matchIsLog
import com.gq.basic.widget.TimberFileTree
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(application: Application) : BasicViewModel(application) {


    fun queryLogsFiles(): LiveData<MutableList<File>> {
        val liveData = getMutableLiveData<MutableList<File>>()
        viewModelScope.launch {
            liveData.value = TimberFileTree.logFilePath.listFiles()?.toMutableList() ?: mutableListOf()
        }
        return liveData
    }

    /**
     * 读取Log内容
     */
    fun readLogContent(file: File): LiveData<String> {
        val liveData = getMutableLiveData<String>()
        if (!file.path.matchIsLog()) {
            liveData.value = "Log format error"
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                var br: BufferedReader? = null
                try {
                    br = BufferedReader(FileReader(file))
                    val sb = StringBuilder()
                    br.forEachLine { str ->
                        sb.append(str).append("\n")
                    }
                    liveData.postValue(sb.toString())
                } catch (e: Exception) { Timber.e(e) } finally {
                    br?.close()
                }
            }
        }
        return liveData
    }

}