package com.gq.basic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gq.basic.AppContext
import com.gq.basic.R
import kotlinx.coroutines.*

/**
 * 加载Dialog
 */
@Composable
fun LoadingDialogCompose(
    loadingDialogState: LoadingDialogState,
) {
    val coroutineScope = rememberCoroutineScope()
    if (loadingDialogState.isShowDialog) {
        var countDownText by remember { mutableStateOf("") }
        DisposableEffect(key1 = loadingDialogState.isShowDialog, effect = {
            var job: Job? = null
            if (loadingDialogState.isShowDialog && loadingDialogState.duration > 20000L) {
                job = coroutineScope.launch {
                    withContext(Dispatchers.Default) {
                        do {
                            loadingDialogState.duration -= 1000
                            if (loadingDialogState.duration < 20000L) {
                                countDownText = "${loadingDialogState.duration / 1000}"
                            }
                            delay(1000)
                        } while (loadingDialogState.duration > 0)
                        loadingDialogState.isShowDialog = false
                    }
                }
            }

            onDispose {
                job?.cancel()
            }
        })

        Dialog(
            onDismissRequest = { loadingDialogState.isShowDialog = false },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(vertical = 9.dp, horizontal = 16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(33.dp)
                    )
                    Text(text = countDownText, fontSize = 11.sp)
                }

                if (!loadingDialogState.isHideText) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = loadingDialogState.text,
                        fontSize = loadingDialogState.fonSize.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun rememberLoadingDialogState(isShow: Boolean): LoadingDialogState = remember {
    LoadingDialogState().apply {
        isShowDialog = isShow
    }
}

@Stable
class LoadingDialogState {
    var isShowDialog: Boolean by mutableStateOf(false)
    var isHideText: Boolean by mutableStateOf(false)
    var text: String by mutableStateOf(AppContext.application.getString(R.string.cb_loading))
    var fonSize: Int by mutableStateOf(12)

    // 20秒 自动关闭
    internal var duration by mutableStateOf(25000L)

    fun showLoading(
        charSequence: String = AppContext.application.getString(R.string.cb_loading),
        isHideText: Boolean = false,
        duration: Long = 25000L,
    ) {
        this.duration = duration
        this.isHideText = isHideText
        text = charSequence
        isShowDialog = true
    }

    fun showLoading(stringResId: Int, isHideText: Boolean = false, duration: Long = 25000L) {
        showLoading(charSequence = AppContext.application.getString(stringResId),
            isHideText = isHideText,
            duration = duration)
    }

    fun hideLoading() {
        isShowDialog = false
    }

}