package com.gq.composefoundation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastAny
import androidx.navigation.NavController
import com.gq.basic.common.ToastCommon
import com.gq.composefoundation.R
import timber.log.Timber

/**
 * 手势测试
 */
@Composable
fun GestureTestCompose(navController: NavController) {
    Scaffold {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            var offset by remember { mutableStateOf(Offset.Zero) }
            var scale by remember { mutableStateOf(1f) }
            var doubleTapTimestamp by remember { mutableStateOf(0L) }
            var tapTimestamp by remember { mutableStateOf(0L) }
            var onImageIntSize by remember { mutableStateOf(IntSize.Zero) }

            Image(
                painter = painterResource(id = R.drawable.t7b),
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .onSizeChanged {
                        Timber.i("onSizeChanged->${it}")
                        onImageIntSize = it
                    }
                    .pointerInput(Unit) {
                        forEachGesture {
                            awaitPointerEventScope {
                                val awaitFirstDown: PointerInputChange = awaitFirstDown()

                                do {
                                    if (awaitFirstDown.changedToDown()) {
                                        tapTimestamp = System.currentTimeMillis()
                                        awaitFirstDown.consumeDownChange()
                                    }

                                    val event = awaitPointerEvent()
                                    // 双击
                                    if (event.changes[0].changedToUp()) {
                                        val curTimestamp = System.currentTimeMillis()
                                        if (curTimestamp - doubleTapTimestamp in 151..599) {
                                            ToastCommon.showCenterToast("双击")
                                            event.changes[0].consumeAllChanges()
                                        }
                                        doubleTapTimestamp = curTimestamp
                                    }

                                    if (event.changes[0].changedToUp()) {
                                        if (System.currentTimeMillis() - tapTimestamp < 151) {
                                            ToastCommon.showCenterToast("单击")
                                            event.changes[0].consumeAllChanges()
                                        }
                                    }


                                    Timber.i("awaitPointerEventScope->awaitFirstUp->${awaitFirstDown.changedToUp()}")
                                    Timber.i("awaitPointerEventScope->awaitFirstDown->${awaitFirstDown.changedToDown()}")
                                    Timber.i("awaitPointerEventScope->changedToUp->${event.changes[0].changedToUp()}")
                                    Timber.i("awaitPointerEventScope->changedToDown->${event.changes[0].changedToDown()}")

                                    scale *= event.calculateZoom()
                                    offset += event.calculatePan() * scale
                                    event.changes.forEach {
                                        it.consumePositionChange()
                                    }


                                } while (event.changes.fastAny { it.pressed })
                            }
                        }


                    }
            )
        }
    }
}