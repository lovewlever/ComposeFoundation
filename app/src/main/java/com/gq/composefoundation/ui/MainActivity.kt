package com.gq.composefoundation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.gq.basic.basis.BasicActivity
import com.gq.basic.common.*
import com.gq.basic.compose.*
import com.gq.composefoundation.ui.theme.ComposeFoundationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BasicActivity() {

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DirCommon.getCacheDirFile("image")
        setContent {
            ComposeFoundationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ProvideWindowInsets {
                        Greeting("Android")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun Greeting(name: String) {
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
        //PictureAndVideoSelectorCompose()
        CheckUpdateAppCompose(
            versionName = "1.2.3",
            downloadUrl = "",
            isShowDialogState = remember { mutableStateOf(true) }
        )
    }

}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeFoundationTheme {
        Greeting("Android")
    }
}