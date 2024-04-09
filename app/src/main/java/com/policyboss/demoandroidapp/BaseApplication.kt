package com.policyboss.demoandroidapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application() {


    companion object {
        const val CHANNEL_ID = "com.policyboss.demoApp.channelID002"
        const val CHANNEL_NAME = "com.policyboss.demoApp.channelDemo2"
        const val NOTIFICATION_ID = 1 // Unique notification ID
    }


    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        //region comment
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "location",
//                "Location",
//                NotificationManager.IMPORTANCE_LOW
//            )
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }

        //endregion
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                // Customize channel settings (description, importance, lights, etc.)
                description = "Your channel description"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}