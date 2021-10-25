package com.gq.composefoundation.ui

import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.gq.basic.basis.BasicActivity
import com.gq.basic.common.DeviceCommon
import com.gq.basic.common.DirCommon
import com.gq.composefoundation.NavGraph
import com.gq.composefoundation.R
import com.gq.composefoundation.ui.fragment.Fragment1
import com.gq.composefoundation.ui.fragment.Fragment2
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BasicActivity() {

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DirCommon.getCacheDirFile("image")
        setContentView(R.layout.main_activity)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.navController.apply {
            graph = createGraph(NavGraph.Route.Home) {
                this.fragment<Fragment1>(route = NavGraph.Route.Home)
                this.fragment<Fragment2>(route = NavGraph.Route.PlantDetail)
            }
        }


            Timber.i(DeviceCommon.getAndroidId())

    }

    override fun onResume() {
        super.onResume()
    }
}