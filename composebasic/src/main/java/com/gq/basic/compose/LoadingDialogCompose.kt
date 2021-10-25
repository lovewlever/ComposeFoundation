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

/**
 * 加载Dialog
 */
@Composable
fun LoadingDialogCompose(
    loadingDialogState: LoadingDialogState
) {
    if (loadingDialogState.isShowDialog) {
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
                CircularProgressIndicator(
                    modifier = Modifier.size(33.dp)
                )
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
}