package com.madispace.worldofmothers.ui.common

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.madispace.worldofmothers.R

object NotificationFactory {
    fun createUploadNotification(
        context: Context,
        title: String,
        actionText: String,
        intent: PendingIntent
    ): Notification {
        return getNotificationBuilder(context)
            .setContentTitle(title)
            .setOngoing(true)
            .setProgress(0, 0, true)
            .addAction(R.drawable.ic_delete_white, actionText, intent)
            .build()
    }

    private fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager).also {
                builder.setChannelId(it.id)
            }
        }
        return builder
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }

    private const val CHANNEL_ID = "WorldOfMothers"
}