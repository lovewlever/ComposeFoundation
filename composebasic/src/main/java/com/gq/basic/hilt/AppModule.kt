package com.gq.basic.hilt

import com.gq.basic.common.SystemUiController
import com.gq.basic.common.SystemUiControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
abstract class AppModule {

    @Binds
    abstract fun bindsSystemUiController(systemUiControllerImpl: SystemUiControllerImpl): SystemUiController
}