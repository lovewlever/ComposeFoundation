package com.gq.basic.basis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gq.basic.common.SystemUiController
import com.gq.basic.common.SystemUiControllerImpl


open class BasicNoHiltFragment: Fragment() {

    protected var systemUiController: SystemUiController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { systemUiController = SystemUiControllerImpl(it) }
    }

    override fun onResume() {
        super.onResume()
        systemUiController
            ?.setDecorFitsSystemWindows()
            ?.setStatusBarColor()
            ?.setNavigationBarColor()
    }
}