package com.gq.composefoundation.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import com.gq.basic.common.DensityCommon
import com.gq.basic.compose.*
import com.gq.composefoundation.ui.graph.ScreenRoute
import com.gq.composefoundation.ui.theme.Shapes
import com.gq.composefoundation.viewmodel.AppViewModel
import kotlinx.coroutines.launch

/**
 * 首页
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FirstPageCompose(
    navController: NavController,
    appViewModel: AppViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val pictureVideoSelectorState = rememberPictureVideoSelectorState(
        quantityLimit = 4,
        type = PVUris.TYPE_PV_ALL, chooseModel = PVUris.CM_VIDEO_MULTIPLE)
    LaunchedEffect(key1 = true, block = {
        appViewModel.getAppConfig()
    })

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = Shapes.large,
        sheetContent = {
            Column {
                Spacer(modifier = Modifier.height(0.1.dp))
                PictureAndVideoSelectorCompose(
                    modifier = Modifier
                        .height((DensityCommon.getScreenHeightDp() - 50).dp),
                    selectorState = pictureVideoSelectorState, onCloseClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    }, onCompleteClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }
                    })
            }
        }) {
        Scaffold {
            val list = arrayOf(
                "手势测试" to {
                    navController.navigate(ScreenRoute.GestureTest.route)
                },
                "Logs" to {
                    navController.navigate(ScreenRoute.LogListTest.route)
                },
            )
            LazyVerticalGrid(columns = GridCells.Fixed(3),
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(it),
                content = {
                    items(list) { pair ->
                        Button(onClick = {
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                            pair.second()
                        }) {
                            Text(text = pair.first)
                        }
                    }
                })
            val checkUpdateState = rememberCheckUpdateState()
            checkUpdateState.updateIllustrate = "1.更新说明，\n2.更新说明更新说明更\n3.新说明更新说明更新说明"
            checkUpdateState.downloadUrl = "https"
            checkUpdateState.showVersionName = "v1.1.0"
            checkUpdateState.isShowDialog = true
            CheckUpdateAppCompose(checkUpdateState = checkUpdateState)
        }

        LoadingDialogCompose(loadingDialogState = rememberLoadingDialogState(isShow = true))
    }

}