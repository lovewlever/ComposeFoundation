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
    return slideIn(animationSpec = spring(stiffness = 200F), initialOffset = {
        IntOffset(x = 0, y = 500)
    }) + fadeIn(animationSpec = tween(800))
}

/**
 * 指定此目的地在经过 . 后重新进入屏幕时运行的动画popBackStack()。这默认为enterTransition
 */
fun popEnterTransition(): EnterTransition {
    return slideIn(animationSpec = spring(stiffness = 50F), initialOffset = {
        IntOffset(x = 0, y = -200)
    }) + fadeIn(animationSpec = tween(800))
}

/**
 * 指定当您通过导航到另一个目的地离开此目的地时运行的动画。
 */
fun exitTransition(): ExitTransition {
    return slideOut(animationSpec = spring(stiffness = 50F), targetOffset = {
        IntOffset(x = 0, y = -200)
    }) + fadeOut(targetAlpha = 0.5F, animationSpec = tween(800))
}

/**
 * 指定在您将其从返回堆栈中弹出后，当此目标离开屏幕时运行的动画。这默认为exitTransition.
 */
fun popExitTransition(): ExitTransition {
    return slideOut(animationSpec = spring(stiffness = 200F), targetOffset = {
        IntOffset(x = 0, y = 500)
    }) + fadeOut(animationSpec = tween(400))
}