package com.gq.basic.compose

import android.Manifest
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.gq.basic.R
import com.gq.basic.common.DensityCommon
import com.gq.basic.common.loadVideoThumbnail
import com.gq.basic.common.millisecondToHms
import com.gq.basic.viewmodel.PictureVideoSelectorViewModel

data class PVUris(
    var uri: Uri,
    var name: String,
    var size: Int,
    var type: Int,
    var duration: Int = 0
) {
    companion object {
        const val TYPE_PICTURE = 0
        const val TYPE_VIDEO = 1
    }

    var selected: Boolean by mutableStateOf(false)
}

@Composable
fun rememberPictureVideoSelectorState(
    type: Int = PVUris.TYPE_PICTURE,
    quantityLimit: Int = -1,
    chooseMode: Int = PictureVideoSelectorState.CHOOSE_MODE_MULTIPLE
): PictureVideoSelectorState {
    return remember {
        PictureVideoSelectorState().also { ps: PictureVideoSelectorState ->
            ps.type = type
            ps.quantityLimit = quantityLimit
            ps.chooseMode = chooseMode
        }
    }
}

@Stable
class PictureVideoSelectorState {

    companion object {
        const val CHOOSE_MODE_MULTIPLE = 0
        const val CHOOSE_MODE_SINGLE = 1
    }

    var type: Int by mutableStateOf(PVUris.TYPE_PICTURE)
    val chooseUris = mutableStateListOf<PVUris>()
    var quantityLimit by mutableStateOf(-1)
    var chooseMode: Int by mutableStateOf(CHOOSE_MODE_MULTIPLE)

    fun onSelectedClick(item: PVUris) {
        if (chooseMode == CHOOSE_MODE_MULTIPLE) {
            // 不是无限选择 并且有数量限制，达到数量限制返回
            if (!item.selected
                && quantityLimit != -1
                && chooseUris.size >= quantityLimit
            ) return
            item.selected = !item.selected
            if (item.selected)
                chooseUris.add(item)
            else
                chooseUris.remove(item)
        }
    }
}

/**
 * 图片/视频选择
 */
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun PictureAndVideoSelectorCompose(
    modifier: Modifier = Modifier,
    selectorState: PictureVideoSelectorState = rememberPictureVideoSelectorState(
        quantityLimit = 5,
        type = PVUris.TYPE_PICTURE
    ),
    completeClick: () -> Unit = {},
    closeClick: () -> Unit = {}
) {

    val pvsViewModel: PictureVideoSelectorViewModel = viewModel()

    val readStoragePermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it && selectorState.type == PVUris.TYPE_PICTURE)
                pvsViewModel.queryPicUriList()
            else if (it && selectorState.type == PVUris.TYPE_VIDEO)
                pvsViewModel.queryVideoUriList()
        }

    SideEffect {
        readStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val previewUri = remember {
        mutableStateOf<PVUris?>(null)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {

            // 头部
            PVSTitleBar(
                selectorState = selectorState,
                completeClick = completeClick,
                closeClick = closeClick
            )

            val wh = (DensityCommon.getScreenWidthDp() / 3) - DensityCommon.dip2px(2f)
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                cells = GridCells.Fixed(3),
                contentPadding = PaddingValues(2.dp)
            ) {
                items(pvsViewModel.urisState) { item ->
                    PVSListItem(
                        item = item,
                        wh = wh,
                        selectorState = selectorState,
                        previewUri = previewUri
                    )
                }
            }
        }

        // 预览
        PVSCheckView(
            modifier.align(Alignment.BottomEnd),
            uri = previewUri
        )
    }

}

/**
 * 头
 */
@Composable
private fun PVSTitleBar(
    modifier: Modifier = Modifier,
    selectorState: PictureVideoSelectorState,
    completeClick: () -> Unit = {},
    closeClick: () -> Unit = {}
) {
    Surface() {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {

            Icon(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(30.dp)
                    .height(30.dp)
                    .align(Alignment.CenterStart)
                    .clickable {
                        closeClick()
                    },
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
            Text(
                text = "${selectorState.chooseUris.size}/${selectorState.quantityLimit}",
                modifier = Modifier.align(Alignment.Center)
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Button(
                    onClick = { completeClick() },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .height(30.dp)
                ) {
                    Text(text = stringResource(R.string.cb_define))
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

/**
 * 列表的Item
 */
@Composable
private fun PVSListItem(
    item: PVUris,
    wh: Int,
    selectorState: PictureVideoSelectorState,
    previewUri: MutableState<PVUris?>? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .clickable {
                previewUri?.value = item
                //selectorState.onSelectedClick(item)
            }
            .border(
                width = 2.dp,
                color = if (item.selected) MaterialTheme.colors.secondary else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Image(
            modifier = Modifier
                .size(wh.dp)
                .clip(
                    shape = RoundedCornerShape(4.dp)
                ),
            painter = rememberImagePainter(
                data = if (item.type == PVUris.TYPE_PICTURE) item.uri else item.uri.loadVideoThumbnail(),
                builder = {
                    this.crossfade(true)
                }
            ),
            contentScale = ContentScale.Crop,
            contentDescription = "uri"
        )
        Checkbox(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(30.dp),
            checked = item.selected,
            onCheckedChange = { selectorState.onSelectedClick(item) }
        )
        if (item.type == PVUris.TYPE_VIDEO) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_cf_video),
                contentDescription = "ic_cf_video",
                tint = Color.White
            )

            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 2.dp),
                text = item.duration.millisecondToHms(),
                fontSize = 10.sp,
                color = Color.White
            )
        }
    }
}

/**
 * 预览
 */
@ExperimentalMaterialApi
@Composable
private fun PVSCheckView(
    modifier: Modifier = Modifier,
    uri: MutableState<PVUris?>? = null
) {

    var enable by remember {
        mutableStateOf(false)
    }

    val transition = updateTransition(targetState = enable, label = "")
    val imageWidth = transition.animateDp(
        transitionSpec = {
            tween(durationMillis = 500, easing = FastOutSlowInEasing)
        }, label = ""
    ) {
        if (it) DensityCommon.getScreenWidthDp().dp else 50.dp
    }

    val animScreenHeight = transition.animateDp(
        transitionSpec = {
            tween(durationMillis = 500, easing = FastOutSlowInEasing)
        }, label = ""
    ) {
        if (it) DensityCommon.getScreenHeightDp().dp else 0.dp
    }


    LaunchedEffect(key1 = uri?.value) {
        uri?.value?.let {
            enable = true
        }
    }

    Surface(
        modifier = modifier
            .height(animScreenHeight.value)
            .width(DensityCommon.getScreenWidthDp().dp),
    ) {

        var videoViewState by remember {
            mutableStateOf<VideoView?>(null)
        }

        DisposableEffect(key1 = Unit, effect = {
            onDispose {
                videoViewState?.stopPlayback()
            }
        })

        Box(modifier = Modifier.fillMaxSize()) {
            if (uri?.value?.type == PVUris.TYPE_PICTURE) {
                Image(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .height(animScreenHeight.value)
                        .width(imageWidth.value),
                    painter = rememberImagePainter(
                        data = uri.value?.uri,
                        builder = {
                            this.crossfade(true)
                        }
                    ),
                    contentDescription = ""
                )
            } else if (uri?.value?.type == PVUris.TYPE_VIDEO) {
                AndroidView(
                    modifier = Modifier
                        .height(animScreenHeight.value)
                        .width(imageWidth.value),
                    factory = { ctx ->
                        VideoView(ctx).also {
                            it.setMediaController(MediaController(ctx))
                        }
                    },
                    update = { videoView ->
                        videoViewState = videoView
                        videoView.setVideoURI(uri.value?.uri)
                        videoView.start()
                    }
                )
            }

            Icon(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp)
                    .width(30.dp)
                    .height(30.dp)
                    .clickable {
                        enable = !enable
                    },
                imageVector = Icons.Default.Close,
                contentDescription = ""
            )
        }
    }

}