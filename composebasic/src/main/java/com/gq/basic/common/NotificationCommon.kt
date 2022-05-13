package com.gq.basic.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gq.basic.AppContext

object NotificationCommon {

    private var notifyId = 0
    private const val SummaryId = 43
    private val GROUP_KEY_WORK = "group.key.work.${VersionCommon.getApplicationId()}"
    private val CHANNEL_ID by lazy {
        "${VersionCommon.getApplicationId()}.channel_id"
    }

    private val notificationManager by lazy {
        NotificationManagerCompat.from(AppContext.application)
    }

    fun getNotification(build: NotificationCompat.Builder.() -> NotificationCompat.Builder): Pair<Int, Notification> {
        notifyId++
        return notifyId to NotificationCompat.Builder(AppContext.application, CHANNEL_ID)
            .also { build(it) }
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build()
    }


    fun createNotificationChannel(channelName: String = "ChannelNotification", description: String = "Description") {
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                this.description = description
            }
            val notificationManager: NotificationManager =
                AppContext.application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun cancel(id: Int) {
        notificationManager.cancel(id)
    }
}
