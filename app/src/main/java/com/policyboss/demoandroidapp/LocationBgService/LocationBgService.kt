package com.policyboss.demoandroidapp.LocationBgService

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationBgService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationBgClient


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        //region Comment gettion Location disable or not
//        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            // Use isLocationEnabled directly for API 28 and above
//            if (!locationManager.isLocationEnabled) {
//                // Location services are disabled
//                stop()
//            }
//        } else {
//            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // Location services are disabled
//                stop()
//            }
//        }
        //endregion

        when(intent?.action) {
            ACTION_START -> start()

            ACTION_STOP -> stop()

            else -> super.onStartCommand(intent, flags, startId)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, LocationBackgrounDemActivity::class.java),
            PendingIntent.FLAG_MUTABLE
        )
        val stopIntent = Intent(this, LocationBgService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent,  PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: ")
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L)
            .catch {
                    e -> e.printStackTrace()
            }
            .onEach { location ->
//                val lat = location.latitude.toString().takeLast(3)
//                val long = location.longitude.toString().takeLast(3)

                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(1, updatedNotification.build())

                //Send Broad Cast
                val intent = Intent(LocationBgService.Location_UPDATE)
                    .putExtra("lat", lat)
                    .putExtra("lon", long)
                sendBroadcast(intent)
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val Location_UPDATE = "Location_UPDATE"
    }
}