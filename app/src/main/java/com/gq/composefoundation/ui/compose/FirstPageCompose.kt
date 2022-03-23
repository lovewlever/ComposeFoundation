package com.gq.composefoundation.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import com.gq.basic.compose.CheckUpdateAppCompose
import com.gq.basic.compose.rememberCheckUpdateState
import com.gq.composefoundation.ui.graph.ScreenRoute

/**
 * 首页
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FirstPageCompose(navController: NavController) {
    Scaffold {
        LazyVerticalGrid(cells = GridCells.Fixed(3), modifier = Modifier.statusBarsPadding(), content = {
            item {
                Button(onClick = {
                    navController.navigate(ScreenRoute.GestureTest.route)
                }) {
                    Text(text = "手势测试")
                }
            }
        })

        CheckUpdateAppCompose(checkUpdateState = rememberCheckUpdateState().also {
            it.isShowDialog = true
            it.showVersionName = "vDERFG"
        })
    }
}