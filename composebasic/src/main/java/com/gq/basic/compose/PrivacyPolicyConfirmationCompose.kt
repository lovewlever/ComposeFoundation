package com.gq.basic.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gq.basic.R
import com.gq.basic.common.DataStoreCommon
import com.gq.basic.theme.DividerColor
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * App启动时隐私政策提醒
 */
@Composable
fun PrivacyPolicyConfirmationDialogCompose(
    cancelText: String = stringResource(id = R.string.cb_do_not_use),
    confirmText: String = stringResource(id = R.string.cb_agree),
    cancelTextColor: Color = MaterialTheme.colors.onSurface,
    confirmTextColor: Color = MaterialTheme.colors.primary,
    doneClick: () -> Unit = {},
    refuseClick: () -> Unit = {},
    onUserAgreementClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    // 是否需要显示隐私政策弹窗
    val isShowPP = rememberSaveable {
        mutableStateOf(1)
    }
    // 查询是否已同意使用app
    LaunchedEffect(key1 = Unit) {
        DataStoreCommon.getBasicType(DataStoreCommon.DSK_PRIVACY_POLICY, 0) {
            isShowPP.value = it
            if (isShowPP.value == 1) doneClick()
        }
    }
    if (isShowPP.value != 1) {
        Dialog(onDismissRequest = {  }) {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier
                            .width(320.dp)
                            .padding(top = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.cb_privacy_policy_title))
                        Spacer(modifier = Modifier.height(14.dp))
                        ClickableText(
                            style = TextStyle(color = MaterialTheme.colors.onSurface),
                            modifier = Modifier.padding(horizontal = 14.dp),
                            text = buildAnnotatedString {
                                append(stringResource(R.string.cb_read_carefully_pp))
                                val privacyPolicy = stringResource(R.string.cb_privacy_policy_num)
                                val userAgreement = stringResource(R.string.cb_user_agreement)
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colors.primary
                                    )
                                ) {
                                    append(userAgreement)
                                }
                                append(stringResource(R.string.cb_and))
                                withStyle(
                                    style = SpanStyle(
                                        color = MaterialTheme.colors.primary
                                    )
                                ) {
                                    append(privacyPolicy)
                                }
                                append(stringResource(R.string.cb_pp_other))
                            },
                            onClick = {
                                Timber.i("$it")
                                if (it in 5..11) {
                                    onUserAgreementClick()
                                } else if (it in 13..19) {
                                    onPrivacyPolicyClick()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        DialogBottomDoubleButton2(
                            cancelText = cancelText,
                            confirmText = confirmText,
                            cancelTextColor = cancelTextColor,
                            confirmTextColor = confirmTextColor,
                            onCancelClick = refuseClick,
                            onConfirmClick = {
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
