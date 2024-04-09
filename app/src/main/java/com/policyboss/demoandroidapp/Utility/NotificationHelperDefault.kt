package com.policyboss.demoandroidapp.Utility

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.policyboss.demoandroidapp.R

object NotificationHelperDefault {

    private lateinit var notificationManager: NotificationManagerCompat

    private const val CHANNEL_ID = "com.policyboss.demoApp.channelID001"
    private const val CHANNEL_NAME = "com.policyboss.demoApp.channelDemo"
    var NOTIFICATION_ID  =  (Math.round(Math.random()*1000)).toInt()

    fun initialize(context: Context) {
        notificationManager = NotificationManagerCompat.from(context.applicationContext)
        createNotificationChannel(context.applicationContext)
    }

    fun showNotification(context: Context, title: String, body: String) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Notification Permission
        if (ActivityCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            return
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "com.policyboss.demoApp.channelID001"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
