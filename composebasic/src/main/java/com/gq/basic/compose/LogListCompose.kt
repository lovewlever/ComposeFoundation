package com.gq.basic.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.gq.basic.common.ToastCommon
import com.gq.basic.viewmodel.LogViewModel
import java.io.File

@Composable
fun LogListCompose(
    modifier: Modifier = Modifier,
    logViewModel: LogViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val logFilesState = remember { mutableStateListOf<File>() }
    val owner = LocalLifecycleOwner.current
    val observer by remember {
        mutableStateOf(Observer<MutableList<File>> { fList ->
            swipeRefreshState.isRefreshing = false
            logFilesState.clear()
            logFilesState.addAll(fList)
        })
    }
    var logContentState by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true, block = {
        logViewModel.queryLogsFiles().observe(owner, observer)
    })

    Box(modifier = modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = logContentState == "") {
            SwipeRefresh(state = swipeRefreshState, onRefresh = {
                swipeRefreshState.isRefreshing = true
                logViewModel.queryLogsFiles().observe(owner, observer)
            }) {
                LazyColumn(modifier = Modifier.fillMaxWidth(), content = {
                    items(logFilesState) { file ->
                        Row(
                            modifier = Modifier
                                .clickable {
                                    logViewModel
                                        .readLogContent(file)
                                        .observe(owner, Observer { str ->
                                            logContentState = str
                                        })
                                }
                                .padding(horizontal = 14.dp)
                                .padding(top = 14.dp)
                        ) {
                            Column {
                                Text(text = file.name)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "${file.length()} bytes", fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.weight(1f))

                            Button(onClick = {
                                ToastCommon.showCenterToast("Can't delete temporarily")
                            }) {
                                Text(text = "Delete")
                            }
                        }
                    }
                })
            }
        }

        AnimatedVisibility(visible = logContentState != "") {
            CheckLogTextCompose(logContentState) {
                logContentState = ""
            }
        }

    }
}

@Composable
private fun CheckLogTextCompose(str: String, onCloseClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(content = {
            item {
                Text(text = str)
            }
        })

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 14.dp), onClick = { onCloseClick() }) {
            Text(text = "关闭")
        }
    }
}