package com.gq.basic.basis

import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.gq.basic.common.SystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BasicFragment: Fragment() {

    /*@Inject
    lateinit var systemUiController: SystemUiController*/

    override fun onResume() {
        super.onResume()
        /*systemUiController
            .setDecorFitsSystemWindows()
            .setStatusBarColor()
            .setNavigationBarColor()*/
        setImmersiveMode()
    }

    private fun setImmersiveMode() {
        activity?.let { act ->
            WindowCompat.setDecorFitsSystemWindows(act.window, false)
            act.window.statusBarColor = android.graphics.Color.TRANSPARENT
            act.window.navigationBarColor = android.graphics.Color.TRANSPARENT
        }
    }
}