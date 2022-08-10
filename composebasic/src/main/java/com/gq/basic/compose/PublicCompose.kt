package com.gq.basic.compose


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

/**
 * 弹窗下面的双按钮
 */
@Composable
fun DialogBottomDoubleButton(
    refuseText: String = stringResource(id = R.string.cb_do_not_use),
    doneText: String = stringResource(id = R.string.cb_agree),
    refuseTextColor: Color = MaterialTheme.colors.onPrimary,
    doneTextColor: Color = MaterialTheme.colors.surface,
    refuseTextSize: TextUnit = 14.sp,
    doneTextSize: TextUnit = 14.sp,
    doneClick: () -> Unit = {},
    refuseClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
    ) {
        TextButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            onClick = {
                refuseClick()
            }
        ) {
            Text(text = refuseText, color = refuseTextColor, fontSize = refuseTextSize)
        }
        TextButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            onClick = {
                doneClick()
            }
        ) {
            Text(text = doneText, color = doneTextColor, fontSize = doneTextSize)
        }
    }
}


/**
 * 弹窗下面的双按钮
 */
@Composable
fun DialogBottomDoubleButton2(
    cancelText: String = stringResource(id = R.string.cb_do_not_use),
    confirmText: String = stringResource(id = R.string.cb_agree),
    cancelTextColor: Color = MaterialTheme.colors.onPrimary,
    confirmTextColor: Color = MaterialTheme.colors.surface,
    cancelTextSize: TextUnit = 14.sp,
    confirmTextSize: TextUnit = 14.sp,
    onCancelClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
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