package com.gq.basic.common

import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasicGson @Inject constructor() {

    val gson by lazy { Gson() }

}