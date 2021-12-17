package com.gq.basic.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ProgressButtonPreview() {
    ProgressButton(width = 200.dp, height = 40.dp, state = rememberProgressButtonState(), onClick = { /*TODO*/ }) {
        Text(text = "确定")
    }
}

@Stable
class ProgressButtonState {
    var isLoading by mutableStateOf(false)
    var topStartRadius: Dp by mutableStateOf(4.dp)
    var topEndRadius: Dp by mutableStateOf(4.dp)
    var bottomStartRadius: Dp by mutableStateOf(4.dp)
    var bottomEndRadius: Dp by mutableStateOf(4.dp)
    var durationMillis: Int by mutableStateOf(1000)
}

@Composable
fun rememberProgressButtonState(
    radius: Dp = 4.dp,
    durationMillis: Int = 1000
) = remember {
    ProgressButtonState().also { pbs ->
        pbs.topStartRadius = radius
        pbs.topEndRadius = radius
        pbs.bottomStartRadius = radius
        pbs.bottomEndRadius = radius
        pbs.durationMillis = durationMillis
    }
}

@Composable
fun rememberProgressButtonState(
    topStartRadius: Dp = 4.dp,
    topEndRadius: Dp = 4.dp,
    bottomStartRadius: Dp = 4.dp,
    bottomEndRadius: Dp = 4.dp,
    durationMillis: Int = 1000
) = remember {
    ProgressButtonState().also { pbs ->
        pbs.topStartRadius = topStartRadius
        pbs.topEndRadius = topEndRadius
        pbs.bottomStartRadius = bottomStartRadius
        pbs.bottomEndRadius = bottomEndRadius
        pbs.durationMillis = durationMillis
    }
}

@Composable
fun ProgressButton(
    width: Dp,
    height: Dp,
    state: ProgressButtonState,
    onClick: () -> Unit,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable RowScope.() -> Unit
) {

    // 宽度
    val widthAnim = animateDpAsState(targetValue = if (state.isLoading) height else width,
        animationSpec = tween(durationMillis = state.durationMillis), finishedListener = {
            if (state.isLoading) {
                onClick()
            }
    })
    val transition = updateTransition(targetState = state.isLoading, label = "")
    // 进度透明度
    val circularProgressAlphaAnim = transition.animateFloat(
        label = "",
        transitionSpec = { tween(durationMillis = state.durationMillis) }) {
        if (it) 1F else 0F
    }
    // 按钮透明度
    val btnAlphaAnim = transition.animateFloat(
        label = "",
        transitionSpec = { tween(durationMillis = state.durationMillis) }) {
        if (it) 0F else 1F
    }
    // 圆角
    val topStartRadiusAnim = transition.animateDp(
        label = "",
        transitionSpec = { tween(durationMillis = state.durationMillis / 2) }) { if (it) height else state.topStartRadius }
    val topEndRadiusAnim = transition.animateDp(
        label = "",
        transitionSpec = { tween(durationMillis = state.durationMillis / 2) }) { if (it) height else state.topEndRadius }
    val bottomStartRadiusAnim = transition.animateDp(
        label = "",
        transitionSpec = { tween(durationMillis = state.durationMillis / 2) }) { if (it) height else state.bottomStartRadius }
    val bottomEndRadiusAnim = transition.animateDp(
        label = "",
        transitionSpec = { tween(durationMillis = state.durationMillis / 2) }) { if (it) height else state.bottomEndRadius }

    Box(contentAlignment = Alignment.Center) {
        if (btnAlphaAnim.value > 0F) {
            Button(
                modifier = Modifier
                    .size(width = widthAnim.value, height = height)
                    .alpha(btnAlphaAnim.value),
                onClick = {
                    state.isLoading = !state.isLoading
                },
                enabled = enabled,
                interactionSource = interactionSource,
                elevation = elevation,
                shape = RoundedCornerShape(
                    topStart = topStartRadiusAnim.value, topEnd = topEndRadiusAnim.value,
                    bottomStart = bottomStartRadiusAnim.value, bottomEnd = bottomEndRadiusAnim.value
                ),
                border = border,
                colors = colors,
                contentPadding = contentPadding,
                content = content
            )
        }

        CircularProgressIndicator(
            modifier = Modifier
                .size(width = height, height = height)
                .alpha(circularProgressAlphaAnim.value),
            color = colors.backgroundColor(enabled = true).value
        )
    }
}