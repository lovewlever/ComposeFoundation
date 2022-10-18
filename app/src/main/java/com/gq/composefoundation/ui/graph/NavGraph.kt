package com.gq.composefoundation.ui.graph

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry


sealed class ScreenRoute(val route: String, val title: String) {
    object GestureTest :ScreenRoute("gestureTest", "手势测试")
    object FirstPage :ScreenRoute("firstPage", "首页")
    object LogListTest :ScreenRoute("logListTest", "Logs")
}


/**
 * 指定当您navigate()到达此目的地时运行的动画。
 */
fun enterTransition(): EnterTransition {
    return slideIn(animationSpec = spring(stiffness = 150F), initialOffset = {
        IntOffset(x = 200, y = 0)
    }) + fadeIn(animationSpec = tween(700))
}

/**
 * 指定此目的地在经过 . 后重新进入屏幕时运行的动画popBackStack()。这默认为enterTransition
 */
fun popEnterTransition(): EnterTransition {
    return slideIn(animationSpec = spring(stiffness = 150F), initialOffset = {
        IntOffset(x = -200, y = 0)
    }) + fadeIn(animationSpec = tween(700))
}

/**
 * 指定当您通过导航到另一个目的地离开此目的地时运行的动画。
 */
fun exitTransition(): ExitTransition {
    return slideOut(animationSpec = spring(stiffness = 150F), targetOffset = {
        IntOffset(x = -200, y = 0)
    }) + fadeOut(targetAlpha = 0.5F, animationSpec = tween(500))
}

/**
 * 指定在您将其从返回堆栈中弹出后，当此目标离开屏幕时运行的动画。这默认为exitTransition.
 */
fun popExitTransition(): ExitTransition {
    return slideOut(animationSpec = spring(stiffness = 150F), targetOffset = {
        IntOffset(x = 200, y = 0)
    }) + fadeOut(animationSpec = tween(500))
}