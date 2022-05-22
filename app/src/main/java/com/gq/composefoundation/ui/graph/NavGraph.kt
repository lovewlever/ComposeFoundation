package com.gq.composefoundation.ui.graph


sealed class ScreenRoute(val route: String, val title: String) {
    object GestureTest :ScreenRoute("gestureTest", "手势测试")
    object FirstPage :ScreenRoute("firstPage", "首页")
    object LogListTest :ScreenRoute("logListTest", "Logs")
}