package com.gq.basic.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gq.basic.AppContext
import kotlinx.coroutines.delay

class ToastCustomState {
    var isShow by mutableStateOf(false)
    var text by mutableStateOf("")
    internal var duration by mutableStateOf(2000L)

    fun showToast(charSequence: String?, duration: Long = 2000L) {
        if (charSequence != null && charSequence.isNotEmpty()) {
            this.duration = duration
            text = charSequence
            isShow = true
        }
    }

    fun showToast(stringResId: Int, duration: Long = 2000L) {
        this.duration = duration
        text = AppContext.application.getString(stringResId)
        isShow = true
    }

}

@Composable
fun rememberToastCustomState() = remember {
    ToastCustomState()
}

@Composable
fun ToastCustomCompose(
    toastCustomState: ToastCustomState,
    background: Color = Color(0x9F040A23),
) {
    ToastCustomCompose(toastCustomState = toastCustomState, background = background, content = {
        Text(text = toastCustomState.text, fontSize = 14.sp, color = Color.White)
    })
}


@Composable
fun ToastCustomCompose(
    toastCustomState: ToastCustomState,
    background: Color = Color(0x9F040A23),
    content: @Composable RowScope.() -> Unit = {}
) {

    AnimatedVisibility(
        enter = fadeIn(),
        exit =  fadeOut(),
        visible = toastCustomState.isShow) {
        LaunchedEffect(key1 = toastCustomState.isShow, block = {
            if (toastCustomState.isShow) {
                delay(toastCustomState.duration)
                toastCustomState.isShow = false
            }
        })
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier
                .background(background, shape = CircleShape)
                .padding(horizontal = 12.dp, vertical = 11.dp)) {
                content()
            }
        }
    }
}