package com.gq.basic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gq.basic.R
import com.gq.basic.common.ToastCommon
import com.gq.basic.common.matchUrl
import com.gq.basic.hilt.UpdateAppModule
import com.gq.basic.viewmodel.UpdateViewModel
import java.io.File

/**
 * app更新提醒
 */
@Composable
fun CheckUpdateAppCompose(
    modifier: Modifier = Modifier,
    versionName: String,
    downloadUrl: String,
    isShowDialogState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    UpdateRemindDialogCompose(
        modifier = modifier.width(300.dp),
        versionName = versionName,
        downloadUrl = downloadUrl,
        isShowDialogState = isShowDialogState
    )
}

/**
 * 更新提醒
 */
@Composable
private fun UpdateRemindDialogCompose(
    modifier: Modifier = Modifier,
    versionName: String,
    downloadUrl: String,
    isShowDialogState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {

    // 是否开始更新Apk
    val updateApkDialogState = remember {
        mutableStateOf(false)
    }

    if (isShowDialogState.value) {
        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        fontSize = 14.5.sp,
                        text = stringResource(R.string.cb_update_remind)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        text = buildAnnotatedString {
                            append(stringResource(id = R.string.cb_discovery_new_version))
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.secondary
                                )
                            ) {
                                append("v$versionName\n")
                            }
                            append(stringResource(id = R.string.cb_whether_to_update))
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .height(0.7.dp)
                            .background(
                                color = Color.LightGray
                            )
                    )

                    DialogBottomDoubleButton(
                        refuseText = stringResource(id = R.string.cb_do_not_update),
                        doneText = stringResource(R.string.cb_update_now),
                        doneClick = {
                            updateApkDialogState.value = true
                            isShowDialogState.value = false
                        },
                        refuseClick = {
                            isShowDialogState.value = false
                        }
                    )
                }
            }
        }
    }

    //更新App
    DownloadApkInstallCompose(
        modifier = Modifier.width(300.dp),
        isShowDialogState = updateApkDialogState,
        downloadUrl = downloadUrl
    )
}

/**
 * 下载Apk并安装
 */
@Composable
private fun DownloadApkInstallCompose(
    modifier: Modifier = Modifier,
    downloadUrl: String,
    isShowDialogState: MutableState<Boolean> = remember { mutableStateOf(false) }
) {

    if (isShowDialogState.value) {
        val viewModel: UpdateViewModel = viewModel()
        val apkDoneState = viewModel.downloadApkDoneResult.observeAsState()

        val context = LocalContext.current

        var downloadApkProgressState by remember {
            mutableStateOf(0f)
        }

        LaunchedEffect(key1 = Unit, block = {
            if (downloadUrl.matchUrl()) {
                viewModel.downloadApk(downloadUrl)
                UpdateAppModule.downloadApkProgress =
                    { url: String?, bytesRead: Long, contentLength: Long, done: Boolean ->
                        val progress = bytesRead.toFloat() / contentLength
                        downloadApkProgressState = progress
                    }
            } else {
                ToastCommon.showCenterToast(context.getString(R.string.cb_url_fail))
            }
        })

        LaunchedEffect(key1 = apkDoneState.value, block = {
            apkDoneState.value?.let {
                viewModel.installApk(context, File(it))
                isShowDialogState.value = false
            }
        })

        Dialog(onDismissRequest = { }) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.cb_download_ing),
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = downloadApkProgressState
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF3370FF)
                        ),
                        modifier = Modifier.width(90.dp),
                        onClick = {
                            viewModel.cancelDownloadApk()
                            isShowDialogState.value = false
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.cb_cancel),
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}