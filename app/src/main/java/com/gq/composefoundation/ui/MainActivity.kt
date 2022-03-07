package com.gq.composefoundation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.gq.basic.basis.BasicActivity
import com.gq.basic.common.DeviceCommon
import com.gq.basic.common.DirCommon
import com.gq.composefoundation.ui.compose.NavigationHostCompose
import com.gq.composefoundation.ui.theme.ComposeFoundationTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DirCommon.getCacheDirFile("image")
        setContent {
            ComposeFoundationTheme {
                MainCompose()
            }
        }
        Timber.i(DeviceCommon.getAndroidId())

    }

    override fun onResume() {
        super.onResume()
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainCompose() {
    Scaffold {
        val navHostController = rememberAnimatedNavController()
        NavigationHostCompose(navHostController)
    }
}