package com.gq.basic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * App启动时隐私政策提醒
 */
@Composable
fun PrivacyPolicyConfirmationCompose() {

    Dialog(onDismissRequest = { /*TODO*/ }) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Surface(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "隐私政策")
                    Spacer(modifier = Modifier.height(8.dp))
                    ClickableText(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = buildAnnotatedString {
                            append("请仔细阅读")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.secondary
                                )
                            ) {
                                append("《隐私政策》")
                            }
                            append("了解详细信息。如你同意，请点击`同意`开始接受我们的同意")
                        },
                        onClick = {

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        TextButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            onClick = { /*TODO*/ }
                        ) {
                            Text(text = "暂不使用")
                        }
                        TextButton(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            onClick = { /*TODO*/ }
                        ) {
                            Text(text = "同意")
                        }
                    }
                }
            }
        }


    }
}