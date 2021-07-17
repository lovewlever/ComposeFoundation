package com.gq.basic.basis

import androidx.fragment.app.Fragment
import com.gq.basic.common.SystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BasicFragment: Fragment() {

    @Inject
    lateinit var systemUiController: SystemUiController

    override fun onResume() {
        super.onResume()
        systemUiController
            .setDecorFitsSystemWindows()
            .setBarsIconLightColor()
    }
}