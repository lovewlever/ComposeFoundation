package com.gq.composefoundation

import androidx.navigation.NavOptions

object NavGraph {

    object Route {
        const val Home = "toHome"
        const val PlantDetail = "toPlantDetail"
    }

    object Args {
        const val plantId = "plantId"
    }

    fun navOptions(): NavOptions =
        NavOptions.Builder().build()

}