package com.gq.basic.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val Colors.DividerColor: Color
    @Composable get() = if (isSystemInDarkTheme()) MaterialTheme.colors.surface.copy(red = 0.2F, green = 0.2F, blue = 0.2F) else Color(0xFFF8F8F8)