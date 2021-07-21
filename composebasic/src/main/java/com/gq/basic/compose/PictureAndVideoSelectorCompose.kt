package com.gq.basic.compose

import android.Manifest
import android.net.Uri
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.decode.VideoFrameDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import com.gq.basic.R
import com.gq.basic.common.*
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
}

/**
 * 图片选择
 */
@ExperimentalFoundationApi
@Composable
fun PictureAndVideoSelectorCompose(
    modifier: Modifier = Modifier,
    selectorState: PictureVideoSelectorState = rememberPictureVideoSelectorState(
        quantityLimit = 5,
        type = PVUris.TYPE_VIDEO
    )
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


    Column(modifier = modifier.fillMaxSize()) {
        Surface(
            color = MaterialTheme.colors.primary
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            ) {
                Text(
                    text = "${selectorState.chooseUris.size}/${selectorState.quantityLimit}",
                    modifier = Modifier.align(Alignment.Center)
                )
                Button(
                    onClick = { },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(text = "确定")
                }
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            cells = GridCells.Fixed(3),
            contentPadding = PaddingValues(2.dp)
        ) {
            items(pvsViewModel.urisState) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clickable {
                            if (selectorState.chooseMode == PictureVideoSelectorState.CHOOSE_MODE_MULTIPLE) {
                                // 不是无限选择 并且有数量限制，达到数量限制返回
                                if (!item.selected
                                    && selectorState.quantityLimit != -1
                                    && selectorState.chooseUris.size >= selectorState.quantityLimit
                                ) return@clickable
                                item.selected = !item.selected
                                if (item.selected)
                                    selectorState.chooseUris.add(item)
                                else
                                    selectorState.chooseUris.remove(item)
                            }
                        }
                        .border(
                            width = 2.dp,
                            color = if (item.selected) MaterialTheme.colors.secondary else Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    val context = LocalContext.current
                    Image(
                        modifier = Modifier
                            .size((DensityCommon.getScreenWidthDp() / 3).dp)
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
                        modifier = Modifier.align(Alignment.TopEnd),
                        checked = item.selected,
                        onCheckedChange = { item.selected }
                    )
                    if (item.type == PVUris.TYPE_VIDEO) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            painter = painterResource(id = R.drawable.ic_cf_video),
                            contentDescription = "ic_cf_video",
                            tint = Color.White
                        )

                        Text(
                            modifier = modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 4.dp, bottom = 2.dp),
                            text = item.duration.millisecondToHms(),
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}