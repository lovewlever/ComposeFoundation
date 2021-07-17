package com.gq.composefoundation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gq.basic.basis.BasicActivity
import com.gq.basic.common.DirCommon
import com.gq.basic.common.SystemUiController
import com.gq.composefoundation.ui.theme.ComposeFoundationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BasicActivity() {

    @Inject
    lateinit var systemUiController: SystemUiController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DirCommon.getCacheDirFile("image")
        setContent {
            ComposeFoundationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        systemUiController.fullScreen()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeFoundationTheme {
        Greeting("Android")
    }
}