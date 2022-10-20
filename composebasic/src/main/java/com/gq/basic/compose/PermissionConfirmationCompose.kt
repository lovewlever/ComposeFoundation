package com.gq.basic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.gq.basic.common.DataStoreCommon
import kotlinx.coroutines.launch

const val PermissionConfirmAgree = 0
const val PermissionConfirmReject = -1
const val PermissionConfirmNotSet = -2

/**
 * App第一次启动 权限申请提醒
 */
@Composable
fun PermissionConfirmationCompose(
    permissionContent: @Composable () -> Unit,
    onDoneClick: () -> Unit,
    onRejectClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var isShowDialog by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true, block = {
        val i = DataStoreCommon.getBasicType(
            DataStoreCommon.DSK_PERMISSION_CONFIRM,
            PermissionConfirmNotSet
        )
        if (i == PermissionConfirmNotSet) {
            isShowDialog = true
        }
    })
    if (isShowDialog) {

        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .width(400.dp)
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = MaterialTheme.shapes.medium
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(14.dp))
                Text(text = "权限申请", fontSize = 16.sp)
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    permissionContent()
                }
                Spacer(Modifier.height(14.dp))
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF6F6F6),
                    thickness = 0.7.dp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ) {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                DataStoreCommon.putBasicType(
                                    DataStoreCommon.DSK_PERMISSION_CONFIRM,
                                    PermissionConfirmReject
                                )
                                onRejectClick()
                                isShowDialog = false
                            }
                        }, modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(text = "拒绝", fontSize = 14.sp, color = Color.Black)
                    }
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                DataStoreCommon.putBasicType(
                                    DataStoreCommon.DSK_PERMISSION_CONFIRM,
                                    PermissionConfirmAgree
                                )
                                onDoneClick()
                                isShowDialog = false
                            }
                        }, modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Text(text = "同意", fontSize = 14.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}