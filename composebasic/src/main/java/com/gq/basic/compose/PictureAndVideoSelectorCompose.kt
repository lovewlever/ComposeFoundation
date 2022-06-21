package com.gq.basic.compose

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.gq.basic.R
import com.gq.basic.common.DensityCommon
import com.gq.basic.common.ToastCommon
import com.gq.basic.common.millisecondToHms
import com.gq.basic.extension.loadVideoThumbnail
import com.gq.basic.theme.BasicShapes
import com.gq.basic.viewmodel.PictureVideoSelectorViewModel
import kotlinx.coroutines.flow.Flow

data class PVUris(
    var uri: Uri,
    var name: String,
    var size: Int,
    var type: Int,
    var duration: Int = 0,
) {
    companion object {
        const val TYPE_PICTURE = 0
        const val TYPE_VIDEO = 1
        const val TYPE_PV_ALL = 2

        // 图片多选
        const val CM_PICTURE_MULTIPLE = 5

        // 视频多选
        const val CM_VIDEO_MULTIPLE = 6

        // 全多选
        const val CM_ALL_MULTIPLE = 7

        // 全单选
        const val CM_ALL_SINGLE = 8
    }

    var selected: Boolean by mutableStateOf(false)
}

@Composable
fun rememberPictureVideoSelectorState(
    type: Int = PVUris.TYPE_PICTURE,
    quantityLimit: Int = 1,
    chooseModel: Int = PVUris.CM_ALL_MULTIPLE,
): PictureVideoSelectorState {
    return remember {
        PictureVideoSelectorState().also { ps: PictureVideoSelectorState ->
            ps.type = type
            ps.quantityLimit = quantityLimit
            ps.chooseModel = chooseModel
        }
    }
}

@Stable
class PictureVideoSelectorState {
    var type: Int by mutableStateOf(PVUris.TYPE_PICTURE)
    val chooseUris = mutableStateListOf<PVUris>()
    var quantityLimit by mutableStateOf(1)
    var chooseModel by mutableStateOf(PVUris.CM_ALL_MULTIPLE)

    fun removeChoosePVUri(pvUris: PVUris) {
        pvUris.selected = false
        chooseUris.remove(pvUris)
    }
}

/**
 * 图片/视频选择
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PictureAndVideoSelectorCompose(
    modifier: Modifier = Modifier,
    selectorState: PictureVideoSelectorState,
    onCompleteClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
) {

    val owner = LocalLifecycleOwner.current
    val pvsViewModel: PictureVideoSelectorViewModel = viewModel()
    var videoAndPicUriListPager by remember {
        mutableStateOf<Flow<PagingData<PVUris>>?>(null)
    }
    val videoAndPicUriPager = videoAndPicUriListPager?.collectAsLazyPagingItems()
    //var isLoadingDataState by remember { mutableStateOf(false) }
    val readStoragePermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            //isLoadingDataState = true
            val observer = Observer { list: List<PVUris> ->
                //isLoadingDataState = false
            }
            if (it && selectorState.type == PVUris.TYPE_PICTURE)
                pvsViewModel.queryPicUriList().observe(owner, observer)
            else if (it && selectorState.type == PVUris.TYPE_VIDEO)
                pvsViewModel.queryVideoUriList().observe(owner, observer)
            else if (it && selectorState.type == PVUris.TYPE_PV_ALL) {
                videoAndPicUriListPager = pvsViewModel.videoAndPicUriListPager
            }
        }

    LaunchedEffect(key1 = selectorState.type, block = {
        readStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    })

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        val columns = 3
        val rows =
            if (videoAndPicUriPager == null) 0 else ((videoAndPicUriPager.itemCount / columns) + 1)
        val wh = (DensityCommon.getScreenWidthDp() - 2) / 3
        LazyColumn(modifier = Modifier.fillMaxSize(), content = {
            stickyHeader {
                // 头部
                PVSTitleBar(
                    modifier = Modifier.background(color = MaterialTheme.colors.surface),
                    selectorState = selectorState,
                    onCompleteClick = onCompleteClick,
                    onCloseClick = {
                        selectorState.chooseUris.clear()
                        onCloseClick()
                    }
                )
            }
            items(rows) { index -> // 1
                Row(modifier = Modifier
                    .height(wh.dp)
                    .padding(2.dp)) {
                    for (columnIndex in 0 until columns) {
                        //itemIndex List数据位置 0 == 0 1 2 // 1 == 3 4 5
                        val itemIndex = index * 3 + columnIndex
                        if (itemIndex < videoAndPicUriPager!!.itemCount) {
                            val item = videoAndPicUriPager[itemIndex]
                            item?.let {
                                PVSListItem(
                                    modifier = modifier
                                        .weight(1f)
                                        .height(wh.dp),
                                    item = item,
                                    wh = wh,
                                    selectorState = selectorState,
                                    onItemClick = {
                                        // 都是多选的情况
                                        if (selectorState.chooseModel == PVUris.CM_ALL_MULTIPLE) {
                                            // 选择的照片少于指定 num
                                            if (selectorState.chooseUris.size < selectorState.quantityLimit) {
                                                item.selected = !item.selected
                                                if (item.selected) selectorState.chooseUris.add(item) else selectorState.chooseUris.remove(
                                                    item)
                                            } else {
                                                if (item.selected) {
                                                    item.selected = false
                                                    selectorState.chooseUris.remove(item)
                                                }
                                            }
                                        } else if (selectorState.chooseModel == PVUris.CM_ALL_SINGLE) {
                                            // 都是单选的情况
                                            for (i in 0 until videoAndPicUriPager.itemCount) {
                                                videoAndPicUriPager[i]?.selected = false
                                            }
                                            item.selected = true
                                            selectorState.chooseUris.clear()
                                            selectorState.chooseUris.add(item)
                                        } else if (selectorState.chooseModel == PVUris.CM_PICTURE_MULTIPLE) {
                                            // 图片多选的情况
                                            // 如果当前选中的是图片
                                            if (item.type == PVUris.TYPE_PICTURE) {
                                                // 判断里面有没有选择的视频
                                                if (selectorState.chooseUris.any { ur -> ur.type == PVUris.TYPE_VIDEO }) {
                                                    ToastCommon.showCenterToast("视频和图片不能同时选择")
                                                } else {
                                                    // 选择的照片少于指定 num
                                                    if (selectorState.chooseUris.size < selectorState.quantityLimit) {
                                                        item.selected = !item.selected
                                                        if (item.selected) selectorState.chooseUris.add(item) else selectorState.chooseUris.remove(
                                                            item)
                                                    } else {
                                                        if (item.selected) {
                                                            item.selected = false
                                                            selectorState.chooseUris.remove(item)
                                                        }
                                                    }
                                                }
                                            } else {
                                                // 如果当前选中的是视频
                                                // 判断里面有没有选择的图片
                                                if (selectorState.chooseUris.any { ur -> ur.type == PVUris.TYPE_PICTURE }) {
                                                    ToastCommon.showCenterToast("视频和图片不能同时选择")
                                                } else {

                                                    item.selected = !item.selected
                                                    selectorState.chooseUris.clear()
                                                    if (item.selected) selectorState.chooseUris.add(item) else selectorState.chooseUris.remove(item)
                                                    for (i in 0 until videoAndPicUriPager.itemCount) {
                                                        val vap = videoAndPicUriPager[i]
                                                        if (vap != item) {
                                                            vap?.selected = false
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (selectorState.chooseModel == PVUris.CM_VIDEO_MULTIPLE) {
                                            // 视频多选的情况
                                            // 如果当前选中的是视频
                                            if (item.type == PVUris.TYPE_VIDEO) {
                                                // 判断里面有没有选择的视频
                                                if (selectorState.chooseUris.any { ur -> ur.type == PVUris.TYPE_PICTURE }) {
                                                    ToastCommon.showCenterToast("视频和图片不能同时选择")
                                                } else {
                                                    // 选择的照片少于指定 num
                                                    if (selectorState.chooseUris.size < selectorState.quantityLimit) {
                                                        item.selected = !item.selected
                                                        if (item.selected) selectorState.chooseUris.add(item) else selectorState.chooseUris.remove(
                                                            item)
                                                    } else {
                                                        if (item.selected) {
                                                            item.selected = false
                                                            selectorState.chooseUris.remove(item)
                                                        }
                                                    }
                                                }
                                            } else {
                                                // 如果当前选中的是视频
                                                // 判断里面有没有选择的图片
                                                if (selectorState.chooseUris.any { ur -> ur.type == PVUris.TYPE_VIDEO }) {
                                                    ToastCommon.showCenterToast("视频和图片不能同时选择")
                                                } else {

                                                    item.selected = !item.selected
                                                    selectorState.chooseUris.clear()
                                                    if (item.selected) selectorState.chooseUris.add(item) else selectorState.chooseUris.remove(item)
                                                    for (i in 0 until videoAndPicUriPager.itemCount) {
                                                        val vap = videoAndPicUriPager[i]
                                                        if (vap != item) {
                                                            vap?.selected = false
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    })
                            }
                        } else {
                            Spacer(Modifier.weight(1f, fill = true))
                        }
                    }
                }
            }
        })
    }
}

/**
 * 头
 */
@Composable
private fun PVSTitleBar(
    modifier: Modifier = Modifier,
    selectorState: PictureVideoSelectorState,
    onCompleteClick: () -> Unit,
    onCloseClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {

        Icon(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(28.dp)
                .align(Alignment.CenterStart)
                .clip(BasicShapes.large)
                .clickable {
                    onCloseClick()
                },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = ""
        )
        // 如果是多选 且
        if (selectorState.quantityLimit > 1) {
            Text(
                text = "${selectorState.chooseUris.size}/${selectorState.quantityLimit}",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Row(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Button(
                onClick = { onCompleteClick() },
                shape = BasicShapes.large,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(32.dp)
            ) {
                Text(text = stringResource(R.string.cb_define))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

/**
 * 列表的Item
 */
@Composable
private fun PVSListItem(
    modifier: Modifier = Modifier,
    item: PVUris,
    wh: Int,
    selectorState: PictureVideoSelectorState,
    onItemClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(2.dp)
            .clickable {
                onItemClick()
            }
            .border(
                width = 2.dp,
                color = if (item.selected) MaterialTheme.colors.secondary else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        //val imageLoader = ImageLoader.Builder(LocalContext.current).components { VideoFrameDecoder.Factory() }.build()
        SubcomposeAsyncImage(
            modifier = Modifier
                .size(wh.dp)
                .clip(
                    shape = RoundedCornerShape(4.dp)
                ),
            contentScale = ContentScale.Crop,
            model = if (item.type == PVUris.TYPE_PICTURE) item.uri else item.uri.loadVideoThumbnail(),
            contentDescription = "",
            loading = {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(10.dp)) {
                    CircularProgressIndicator()
                }
            })
        if (selectorState.quantityLimit > 1) {
            if (item.type == PVUris.TYPE_PICTURE && selectorState.chooseModel == PVUris.CM_PICTURE_MULTIPLE) {
                Checkbox(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(30.dp),
                    checked = item.selected,
                    onCheckedChange = { onItemClick() }
                )
            } else if (item.type == PVUris.TYPE_VIDEO && selectorState.chooseModel == PVUris.CM_VIDEO_MULTIPLE) {
                Checkbox(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(30.dp),
                    checked = item.selected,
                    onCheckedChange = { onItemClick() }
                )
            }
        }

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