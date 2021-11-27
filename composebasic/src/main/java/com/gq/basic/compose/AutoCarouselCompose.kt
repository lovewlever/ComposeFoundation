package com.gq.basic.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 自动横向轮播
 */
@ExperimentalPagerApi
@Composable
fun AutoHorizontalCarouselCompose(
    modifier: Modifier = Modifier,
    count: Int,
    delay: Long = 3000,
    pagerState: PagerState = rememberPagerState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable PagerScope.(page: Int) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    if (count > 0) {
        DisposableEffect(key1 = Unit, effect = {
            val job = coroutineScope.launch {
                while (true) {
                    delay(delay)
                    var page = pagerState.currentPage + 1
                    if (page > count - 1) {
                        page = 0
                    }
                    pagerState.animateScrollToPage(page)
                }
            }
            onDispose {
                job.cancel()
            }
        })
    }
    Box(modifier = modifier) {
        HorizontalPager(
            count = count,
            state = pagerState,
            contentPadding = contentPadding,
            modifier = Modifier
                .fillMaxSize(),
            content = content)
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

