package com.gq.composefoundation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.gson.JsonObject
import com.gq.basic.basis.BasicFragment
import com.gq.basic.common.DataStoreCommon
import com.gq.basic.common.ToastCommon
import com.gq.basic.compose.CheckUpdateAppCompose
import com.gq.basic.compose.ProgressButton
import com.gq.basic.compose.rememberProgressButtonState
import com.gq.composefoundation.ui.theme.ComposeFoundationTheme

class Fragment1: BasicFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            ComposeFoundationTheme {
                ProvideWindowInsets {
                    Greeting("") {

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
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
        Column {
            ProgressButton(width = 200.dp, height = 40.dp, state = rememberProgressButtonState(), onClick = {
                ToastCommon.showCenterToast("YES")
            }) {
                Text(text = "ProgressButton")
            }
            /*PictureAndVideoSelectorCompose(
                completeClick = {
                    jumpClick()
                }
            )*/
            CheckUpdateAppCompose(
                versionName = "1.2.3",
                downloadUrl = "https://xiuxianhaidiao-apk.oss-cn-beijing.aliyuncs.com/qhd.apk",
                isShowDialogState = remember { mutableStateOf(true) }
            )
        }

    }

}