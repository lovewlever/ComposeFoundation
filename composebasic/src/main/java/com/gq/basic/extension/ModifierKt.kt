package com.gq.basic.extension

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.gq.basic.theme.BasicShapes

@Composable
fun Modifier.placeholderKt(
    visible: Boolean,
    color: Color = MaterialTheme.colors.background,
    shape: Shape = BasicShapes.large,
    highlight: PlaceholderHighlight = PlaceholderHighlight.fade(),
): Modifier {
    return placeholder(
        visible = visible,
        color = color,
        shape = shape,
        /*highlight = PlaceholderHighlight.shimmer(
            highlightColor = MaterialTheme.colors.background,
        )*/
        highlight = highlight
    )
}



@Composable
fun Modifier.placeholderKt2(
    visible: Boolean,
    color: Color = MaterialTheme.colors.background,
    shape: Shape = MaterialTheme.shapes.large,
    highlight: PlaceholderHighlight = PlaceholderHighlight.shimmer(),
): Modifier {
    return placeholder(
        visible = visible,
        color = color,
        shape = shape,
        /*highlight = PlaceholderHighlight.shimmer(
            highlightColor = MaterialTheme.colors.background,
        )*/
        highlight = highlight
    )
}