package com.gq.basic.compose

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Stable
class BannerState<T> {
    var bannerStateList = mutableStateListOf<T>()
    var carouselTimestamp by mutableStateOf(3000L)
}

@Composable
fun <T> rememberBannerState(bannerStateList: SnapshotStateList<T>) = remember {
    BannerState<T>().apply {
        this.bannerStateList = bannerStateList
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun <T> BannerCompose(
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(),
    reverseLayout: Boolean = false,
    itemSpacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    flingBehavior: FlingBehavior = PagerDefaults.flingBehavior(
        state = state,
        endContentPadding = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
    ),
    key: ((page: Int) -> Any)? = null,
    userScrollEnabled: Boolean = true,
    bannerState: BannerState<T>,
    content: @Composable PagerScope.(page: Int, data: T) -> Unit,
) {
    HorizontalPager(
        count = bannerState.bannerStateList.size,
        modifier = modifier,
        state = state,
        reverseLayout = reverseLayout,
        itemSpacing = itemSpacing,
        contentPadding = contentPadding,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        key = key,
        userScrollEnabled = userScrollEnabled,
        content = { page: Int ->
            content(page, bannerState.bannerStateList[page])
        }
    )

    var isOpen by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(key1 = true, effect = {
        coroutineScope.launch(Dispatchers.Main) {
            while (isOpen) {
                delay(bannerState.carouselTimestamp)
                if (bannerState.bannerStateList.size <= 0) break
                Timber.d(bannerState.bannerStateList.size.toString())
                try {
                    if (state.currentPage < bannerState.bannerStateList.size - 1) {
                        state.animateScrollToPage(state.currentPage + 1)
                    } else {
                        state.animateScrollToPage(0)
                    }
                } catch (e: Exception) { }
            }
        }
        onDispose {
            isOpen = false
        }
    })
}