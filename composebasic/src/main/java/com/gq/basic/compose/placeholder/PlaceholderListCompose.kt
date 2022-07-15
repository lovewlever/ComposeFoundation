package com.gq.basic.compose.placeholder

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.gq.basic.theme.BasicShapes


@Composable
fun Modifier.placeholderKt(visible: Boolean): Modifier {
    return placeholder(
        visible = visible,
        color = Color(0xFFE9E8E8),
        shape = BasicShapes.large,
        /*highlight = PlaceholderHighlight.shimmer(
            highlightColor = MaterialTheme.colors.background,
        )*/
        highlight = PlaceholderHighlight.fade()
    )

}

/**
 * 列表
 */
@Composable
fun PlaceholderListCompose(visible: Boolean = true, itemNumber: Int = 5) {
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()) {
        for (i in 0 until itemNumber) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(BasicShapes.large)
                        .placeholderKt(visible)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(BasicShapes.large)
                        .placeholderKt(visible)
                )
            }
        }
    }
}