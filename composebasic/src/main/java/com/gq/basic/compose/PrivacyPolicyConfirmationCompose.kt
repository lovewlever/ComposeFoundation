package com.gq.basic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gq.basic.R
import com.gq.basic.common.DataStoreCommon
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * App启动时隐私政策提醒
 */
@Composable
fun PrivacyPolicyConfirmationDialogCompose(
    doneClick: () -> Unit = {},
    refuseClick: () -> Unit = {},
    examinePrivacyPolicyClick: () -> Unit = {}
) {

    val coroutineScope = rememberCoroutineScope()

    // 是否需要显示隐私政策弹窗
    val isShowPP = rememberSaveable {
        mutableStateOf(1)
    }
    // 查询是否已同意使用app
    LaunchedEffect(key1 = Unit) {
        DataStoreCommon.getBasicType(DataStoreCommon.DSK_PRIVACY_POLICY) {
            isShowPP.value = it ?: 0
            if (isShowPP.value == 1) doneClick()
        }
    }
    if (isShowPP.value != 1) {
        Dialog(onDismissRequest = {  }) {
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.cb_privacy_policy_title))
                        Spacer(modifier = Modifier.height(8.dp))
                        ClickableText(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            text = buildAnnotatedString {
                                append(stringResource(R.string.cb_read_carefully_pp))
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colors.secondary
                                    )
                                ) {
                                    append(stringResource(R.string.cb_privacy_policy_num))
                                }
                                append(stringResource(R.string.cb_pp_other))
                            },
                            onClick = {
                                Timber.i("$it")
                                if (it in 5..10) {
                                    examinePrivacyPolicyClick()
                                }
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .height(16.7.dp)
                                .background(
                                    color = Color.LightGray
                                )
                        )

                        DialogBottomDoubleButton(
                            refuseClick = refuseClick,
                            doneClick = {
                                coroutineScope.launch {
                                    DataStoreCommon.putBasicType(DataStoreCommon.DSK_PRIVACY_POLICY, 1)
                                }
                                isShowPP.value = 1
                                doneClick()
                            }
                        )
                    }
                }
            }


        }
    }
}
