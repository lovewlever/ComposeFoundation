package com.gq.composefoundation.ui.graph

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.navigation.NavBackStackEntry


sealed class ScreenRoute(val route: String, val title: String) {
    object GestureTest :ScreenRoute("gestureTest", "手势测试")
    object FirstPage :ScreenRoute("firstPage", "首页")
    object LogListTest :ScreenRoute("logListTest", "Logs")
}


@OptIn(ExperimentalAnimationApi::class)
fun enterTransition(a: AnimatedContentScope<NavBackStackEntry>): EnterTransition {
    return a.slideIntoContainer(
        AnimatedContentScope.SlideDirection.Up, animationSpec =
        spring(stiffness = 150F)
    )
}


@OptIn(ExperimentalAnimationApi::class)
fun popExitTransition(a: AnimatedContentScope<NavBackStackEntry>): ExitTransition {
    return a.slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Down,
        animationSpec = spring(stiffness = 150F)
    )
}



@OptIn(ExperimentalAnimationApi::class)
fun popEnterTransition(a: AnimatedContentScope<NavBackStackEntry>): EnterTransition {
    return a.slideIntoContainer(
        AnimatedContentScope.SlideDirection.Left,
        animationSpec = spring(stiffness = 150F)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun exitTransition(a: AnimatedContentScope<NavBackStackEntry>): ExitTransition {
    return a.slideOutOfContainer(
        AnimatedContentScope.SlideDirection.Right, animationSpec =
        spring(stiffness = 150F)
    )
}