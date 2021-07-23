package com.gq.basic.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.gq.basic.R

/**
 * Paging3 顶部状态
 */
@Composable
fun PagingLoadStateTopCompose(
    modifier: Modifier = Modifier,
    combinedLoadStates: CombinedLoadStates,
    onRetryClick: () -> Unit = {},
) {
    when (combinedLoadStates.refresh) {
        is LoadState.Error -> {
            Box(
                modifier = modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                PLSError(
                    modifier = modifier,
                    hint = stringResource(id = R.string.cb_re_refresh),
                    onRetryClick = onRetryClick
                )
            }
        }
        is LoadState.NotLoading -> { }
        is LoadState.Loading -> { }
    }

}

/**
 * Paging3 底部状态
 */
@Composable
fun PagingLoadStateCompose(
    modifier: Modifier = Modifier,
    combinedLoadStates: CombinedLoadStates,
    onRetryClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when (combinedLoadStates.prepend) {
            is LoadState.Error -> {
                PLSError(
                    modifier = modifier,
                    onRetryClick = onRetryClick
                )
            }
            is LoadState.NotLoading -> {
                PLSNotLoading(
                    modifier = modifier
                )
            }
            is LoadState.Loading -> {
                PLSLoading(
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun PLSError(
    modifier: Modifier = Modifier,
    hint: String = stringResource(id = R.string.cb_re_load),
    onRetryClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                onRetryClick()
            }
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Text(text = hint)
    }
}

@Composable
private fun PLSLoading(
    modifier: Modifier = Modifier,
) {
    Row {
        CircularProgressIndicator(
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = stringResource(id = R.string.cb_loading_now))
    }
}

@Composable
private fun PLSNotLoading(
    modifier: Modifier = Modifier,
) {
    Row {
        Text(text = stringResource(id = R.string.cb_no_more_data))
    }
}