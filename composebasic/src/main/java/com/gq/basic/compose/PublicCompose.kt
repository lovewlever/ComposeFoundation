package com.gq.basic.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gq.basic.common.DataStoreCommon

/**
 * 弹窗下面的双按钮
 */
@Composable
fun DialogBottomDoubleButton(
    refuseText: String = "暂不使用",
    doneText: String = "同意",
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
            Text(text = refuseText)
        }
        TextButton(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            onClick = {
                doneClick()
            }
        ) {
            Text(text = doneText)
        }
    }
}