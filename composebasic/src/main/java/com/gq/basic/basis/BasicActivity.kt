package com.gq.basic.basis

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.gq.basic.common.SystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BasicActivity: ComponentActivity() {

    @Inject
    lateinit var systemUiController: SystemUiController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        systemUiController
            .setDecorFitsSystemWindows()
            .setStatusBarColor()
            .setNavigationBarColor()
    }

}