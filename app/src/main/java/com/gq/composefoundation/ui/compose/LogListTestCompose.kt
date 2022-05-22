package com.gq.composefoundation.ui.compose

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.gq.basic.compose.LogListCompose

/**
 * 手势测试
 */
@Composable
fun LogListTestCompose(navController: NavController) {
    Scaffold {
        LogListCompose()
    }
}