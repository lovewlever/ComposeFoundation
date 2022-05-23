package com.gq.basic.basis

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.gq.basic.common.SystemUiController
import com.gq.basic.common.SystemUiControllerImpl

open class BasicNoHiltActivity: ComponentActivity() {

    protected var systemUiController: SystemUiController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        systemUiController = SystemUiControllerImpl(this)
    }

    override fun onResume() {
        super.onResume()
        systemUiController
            ?.setDecorFitsSystemWindows()
            ?.setStatusBarColor()
            ?.setNavigationBarColor()
    }

}