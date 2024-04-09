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
import com.policyboss.demoandroidapp.UI.NavigationComponent.NavigationDemoMainActivity
import com.policyboss.demoandroidapp.UI.NavigationComponent.PushNotificationEntity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationHelperNavGraph  constructor
    ( @ApplicationContext val context: Context,
      private val notificationManager: NotificationManagerCompat){




    fun sendNotification( title: String, message: String, pushNotificationEntity: PushNotificationEntity?) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // we can pass arg and pendingIntent through sendNotification constructor also
// Note : Same as arg req in Destination Fragment
 //       here my dest is also sendcashFragment which generate notification
        val args = Bundle().apply {
            putString(Constant.NOTIFICATION_RECEIVERNAME, "dataValue got from Notification Explicit deeplink") // Optional arguments for the destination


        }

        val pendingIntent = createPendingIntent(R.id.sendCashFragment, args)

       // val notification = buildNotification( title, message, pushNotificationEntity)

        val notification = NotificationCompat.Builder(context, BaseApplication.CHANNEL_ID) // Replace with your channel ID
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(BaseApplication.NOTIFICATION_ID, notification)
    }



    private fun createPendingIntent(destinationId: Int, args: Bundle? = null): PendingIntent {
        val pendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph) // Replace with your navigation graph resource ID
            .setDestination(destinationId)
            .setArguments(args)
            .setComponentName(NavigationDemoMainActivity::class.java)
            .createPendingIntent()


        return pendingIntent
    }


}