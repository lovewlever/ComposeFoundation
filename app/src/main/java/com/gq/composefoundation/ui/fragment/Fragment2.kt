package com.gq.composefoundation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.gq.basic.basis.BasicFragment
import com.gq.basic.compose.WebViewCompose
import com.gq.composefoundation.ui.theme.ComposeFoundationTheme

class Fragment2: BasicFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            ComposeFoundationTheme {
                ProvideWindowInsets {
                    Greeting2("")
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
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
        WebViewCompose(
            url = "https://lovewlever.com/"
        )

    }

}