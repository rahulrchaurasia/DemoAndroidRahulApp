package com.policyboss.demoandroidapp.Utility

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.policyboss.demoandroidapp.BaseApplication
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.HiltModule.AppModules

import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.HomeActivity
import com.policyboss.demoandroidapp.UI.NavigationComponent.PushNotificationEntity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class NotificationHelper @Inject constructor  (@ApplicationContext val context: Context,
                                               @AppModules.ForNotifications  private val notificationManager: NotificationManagerCompat){


    fun sendNotification( title: String, message: String, pushNotificationEntity: PushNotificationEntity?) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(context, BaseApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Optional: Expandable notification
            .setContentIntent(getPendingIntent(pushNotificationEntity))
            .setAutoCancel(true) // Dismiss notification on tap
            .build()

        notificationManager.notify(BaseApplication.NOTIFICATION_ID, notification)
    }



    private fun getPendingIntent(pushNotificationEntity: PushNotificationEntity?): PendingIntent {

        //read json and fill Object with values.
        val intent = Intent(context, HomeActivity::class.java)
        intent.putExtra(Constant.PUSH_NOTITIFICATION, pushNotificationEntity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context, Math.round(Math.random() * 1000).toInt(), intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context, Math.round(Math.random() * 1000).toInt(), intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        return pendingIntent
    }




}