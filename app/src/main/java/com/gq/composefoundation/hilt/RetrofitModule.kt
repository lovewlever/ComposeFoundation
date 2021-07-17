package com.gq.composefoundation.hilt

import com.gq.basic.retrofit.BasicRetrofit
import com.gq.composefoundation.AppApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideAppApi(basicRetrofit: BasicRetrofit): AppApi =
        basicRetrofit.retrofit(AppApi::class)
}