package com.gq.basic.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gq.basic.R
import com.gq.basic.theme.DividerColor

/**
 * 弹窗下面的双按钮
 */
@Deprecated("DialogBottomDoubleButton2")
@Composable
fun DialogBottomDoubleButton(
    refuseText: String = stringResource(id = R.string.cb_do_not_use),
    doneText: String = stringResource(id = R.string.cb_agree),
    doneClick: () -> Unit = {},
    refuseClick: () -> Unit = {},
) {
    DialogBottomDoubleButton2(
        cancelText = refuseText,
        confirmText = doneText,
        onCancelClick = doneClick,
        onConfirmClick = refuseClick,
    )
}


/**
 * 弹窗下面的双按钮
 */
@Composable
fun DialogBottomDoubleButton2(
    cancelText: String = stringResource(id = R.string.cb_do_not_use),
    confirmText: String = stringResource(id = R.string.cb_agree),
    cancelTextColor: Color = MaterialTheme.colors.onSurface,
    confirmTextColor: Color = MaterialTheme.colors.primary,
    cancelTextSize: TextUnit = 14.sp,
    confirmTextSize: TextUnit = 14.sp,
    onCancelClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
) {
    Column {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(0.6.dp)
            .background(color = MaterialTheme.colors.background))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onClick = {
                    onCancelClick()
                }
            ) {
                Text(text = cancelText, color = cancelTextColor, fontSize = cancelTextSize)
            }
            Spacer(modifier = Modifier
                .fillMaxHeight()
                .width(0.5.dp)
                .background(color = MaterialTheme.colors.DividerColor))
            TextButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onClick = {
                    onConfirmClick()
                }
            ) {
                Text(text = confirmText, color = confirmTextColor, fontSize = confirmTextSize)
            }
        }
    }
}