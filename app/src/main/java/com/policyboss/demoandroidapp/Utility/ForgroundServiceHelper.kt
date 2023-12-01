package com.policyboss.demoandroidapp.Utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.work.ForegroundInfo
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.HomeActivity


// More Work in req not in Used
// we have used this thing with directly in workmanager
object ForgroundServiceHelper {

    private lateinit var notificationManager: NotificationManagerCompat

    fun initialize(context: Context) {
        notificationManager = NotificationManagerCompat.from(context)
    }

    fun createForegroundInfo(
        context: Context,
        maxProgress: Int,
        progress: Int,
        strbody: String
    ): ForegroundInfo {

        val id = "com.utility.PolicyBossPro.notifications556"
        val channelName = "SynContact channel"
        val title = "Sync Contact"
        val cancel = "Cancel"
        val body = strbody

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id, channelName)
        }

        val notifyIntent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        notifyIntent.putExtra(Constant.NOTIFICATION_EXTRA, true)
        notifyIntent.putExtra(Constant.NOTIFICATION_PROGRESS, progress)
        notifyIntent.putExtra(Constant.NOTIFICATION_MAX, maxProgress)
        notifyIntent.putExtra(Constant.NOTIFICATION_MESSAGE, strbody)



        val pendingIntent: PendingIntent =
            TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(notifyIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                    ?:PendingIntent.getActivity(context, 0, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                   // throw IllegalStateException("PendingIntent cannot be null")
            }

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationBuilder.setSmallIcon(R.drawable.ic_notifications_24)
            notificationBuilder.color = ContextCompat.getColor(context, R.color.colorPrimary)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_notifications_24)
        }

        notificationBuilder
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(body)
            .setOngoing(false)
            .setProgress(maxProgress, progress, false)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        return ForegroundInfo(1, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                id,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor = Color.BLUE
            channel.description = "PoliyBoss Pro"
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
    }
}
