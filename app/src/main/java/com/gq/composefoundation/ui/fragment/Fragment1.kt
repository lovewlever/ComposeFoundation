package com.gq.composefoundation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.gq.basic.basis.BasicFragment
import com.gq.basic.compose.CheckUpdateAppCompose
import com.gq.basic.compose.PictureAndVideoSelectorCompose
import com.gq.basic.compose.WebViewCompose
import com.gq.composefoundation.NavGraph
import com.gq.composefoundation.ui.theme.ComposeFoundationTheme

class Fragment1: BasicFragment() {

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            ComposeFoundationTheme {
                ProvideWindowInsets {
                    Greeting("") {
                        findNavController()
                            .navigate(
                                route = NavGraph.Route.PlantDetail,
                                navOptions = NavGraph.navOptions()
                            )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Greeting(
    name: String,
    jumpClick: () -> Unit = {}
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsHeight(56.dp)
            ) {

            }
        }
    ) {
        PictureAndVideoSelectorCompose(
            completeClick = {
                jumpClick()
            }
        )
        CheckUpdateAppCompose(
            versionName = "1.2.3",
            downloadUrl = "https://xiuxianhaidiao-apk.oss-cn-beijing.aliyuncs.com/qhd.apk",
            isShowDialogState = remember { mutableStateOf(true) }
        )
    }

}