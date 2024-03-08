package com.policyboss.demoandroidapp.LocationBgService

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.policyboss.demoandroidapp.Utility.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
): LocationBgClient {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long,distance: Float): Flow<Location> {
        return callbackFlow {
            if(!context.hasLocationPermission()) {
                throw LocationBgClient.LocationException("Missing location permission")
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isGpsEnabled && !isNetworkEnabled) {
                throw LocationBgClient.LocationException("GPS is disabled")
            }




            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                // Set the desired update interval in milliseconds (e.g., 300000 for 5 minutes)
                interval
            )
                .setMinUpdateDistanceMeters(distance) // Set the minimum distance between updates (optional)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL) // Use permission-level granularity
                .setWaitForAccurateLocation(true) // Wait for a more accurate location (optional)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}