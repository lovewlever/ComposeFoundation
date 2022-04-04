package com.gq.composefoundation.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import com.gq.basic.common.DensityCommon
import com.gq.basic.compose.*
import com.gq.composefoundation.ui.graph.ScreenRoute
import com.gq.composefoundation.ui.theme.Shapes
import kotlinx.coroutines.launch

/**
 * 首页
 */
@OptIn(ExperimentalFoundationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun FirstPageCompose(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val pictureVideoSelectorState = rememberPictureVideoSelectorState(
        quantityLimit = 4,
        type = PVUris.TYPE_PV_ALL, chooseModel = PVUris.CM_ALL_MULTIPLE)

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
            LazyVerticalGrid(cells = GridCells.Fixed(3),
                modifier = Modifier.statusBarsPadding(),
                content = {
                    item {
                        Button(onClick = {
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }

                            // navController.navigate(ScreenRoute.GestureTest.route)
                        }) {
                            Text(text = "手势测试")
                        }
                    }
                    item {
                        Text(text = "选择数量${pictureVideoSelectorState.chooseUris.size}")
                    }
                })
        }
    }

}