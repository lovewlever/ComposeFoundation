package com.gq.basic.hilt

import com.gq.basic.AppContext
import com.gq.basic.api.UpdateApi
import com.gq.basic.retrofit.ProgressInterceptor
import com.gq.basic.retrofit.ProgressListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpdateAppModule {

    var downloadApkProgress: (
        url: String?,
        bytesRead: Long,
        contentLength: Long,
        done: Boolean,
    ) -> Unit = { _, _, _, _ -> }

    @Singleton
    @Provides
    fun provideUpdateApi(): UpdateApi {
        return Retrofit
            .Builder()
            .baseUrl("https://developer.android.google.cn/")
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(ProgressInterceptor(object : ProgressListener {
                        override fun update(
                            url: String?,
                            bytesRead: Long,
                            contentLength: Long,
                            done: Boolean,
                        ) {
                            downloadApkProgress(url, bytesRead, contentLength, done)
                            Timber.i("${url}--${bytesRead}--${contentLength}--${done}")
                        }
                    })).build()
            ).build().create(UpdateApi::class.java)
    }
}