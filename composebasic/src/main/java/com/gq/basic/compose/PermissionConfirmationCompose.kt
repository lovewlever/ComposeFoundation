package com.gq.basic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * App第一次启动 权限申请提醒
 */
@Composable
fun PermissionConfirmationCompose(
    isShowDialog: MutableState<Boolean>,
    permissionContent: @Composable () -> Unit,
    onDoneClick: () -> Unit,
    onRejectClick: () -> Unit = {}
) {
    if (isShowDialog.value) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier.width(400.dp).background(
                    color = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.medium
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(14.dp))
                Text(text = "权限申请", fontSize = 16.sp)
                Spacer(Modifier.height(14.dp))
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp)) {
                    permissionContent()
                }
                Spacer(Modifier.height(14.dp))
                Divider(modifier = Modifier.fillMaxWidth(), color = Color(0xFFF6F6F6), thickness = 0.7.dp)
                Row(modifier = Modifier.fillMaxWidth().height(45.dp)) {
                    TextButton(onClick = {
                        onRejectClick()
                        isShowDialog.value = false
                    }, modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Text(text = "拒绝", fontSize = 14.sp, color = Color.Black)
                    }
                    TextButton(onClick = {
                        onDoneClick()
                        isShowDialog.value = false
                    }, modifier = Modifier.weight(1f).fillMaxHeight()) {
                        Text(text = "同意", fontSize = 14.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}