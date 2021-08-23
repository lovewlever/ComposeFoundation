package com.gq.composefoundation.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.compose.NavHost
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.setupWithNavController
import com.google.accompanist.insets.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gq.basic.basis.BasicActivity
import com.gq.basic.common.*
import com.gq.basic.compose.*
import com.gq.composefoundation.NavGraph
import com.gq.composefoundation.R
import com.gq.composefoundation.ui.fragment.Fragment1
import com.gq.composefoundation.ui.fragment.Fragment2
import com.gq.composefoundation.ui.theme.ComposeFoundationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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