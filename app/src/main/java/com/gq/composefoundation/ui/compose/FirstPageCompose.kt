package com.gq.composefoundation.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import com.gq.basic.common.DensityCommon
import com.gq.basic.compose.PVUris
import com.gq.basic.compose.PictureAndVideoSelectorCompose
import com.gq.basic.compose.rememberPictureVideoSelectorState
import com.gq.composefoundation.ui.graph.ScreenRoute
import com.gq.composefoundation.ui.theme.Shapes
import kotlinx.coroutines.launch

/**
 * 首页
 */
@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun FirstPageCompose(navController: NavController) {

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val pictureVideoSelectorState = rememberPictureVideoSelectorState(
        quantityLimit = 4,
        type = PVUris.TYPE_PV_ALL, chooseModel = PVUris.CM_VIDEO_MULTIPLE)

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
            LazyVerticalGrid(cells = GridCells.Fixed(3),
                modifier = Modifier.statusBarsPadding(),
                content = {
                    items(list) { pair ->
                        Button(onClick = {
                            /*coroutineScope.launch {
                                bottomSheetState.show()
                            }*/
                            pair.second()
                        }) {
                            Text(text = pair.first)
                        }
                    }
                })
        }
    }

}