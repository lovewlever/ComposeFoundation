package com.gq.composefoundation.ui.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.gq.composefoundation.ui.graph.ScreenRoute

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationHostCompose(navHostController: NavHostController) {
    AnimatedNavHost(navController = navHostController, startDestination = ScreenRoute.FirstPage.route) {
        composable(ScreenRoute.FirstPage.route) {
            FirstPageCompose(navController = navHostController)
        }
        composable(ScreenRoute.GestureTest.route) {
            GestureTestCompose(navController = navHostController)
        }
        composable(ScreenRoute.LogListTest.route) {
            LogListTestCompose(navController = navHostController)
        }
    }
}