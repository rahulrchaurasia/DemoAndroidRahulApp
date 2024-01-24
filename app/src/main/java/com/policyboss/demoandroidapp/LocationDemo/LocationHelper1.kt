package com.policyboss.demoandroidapp.LocationDemo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.policyboss.demoandroidapp.Constant

// Start location updates:
//locationHelper.startLocationUpdates { location ->
//    // Use the received location object for your purposes
//}
//
//locationHelper.stopLocationUpdates(locationCallback) // Pass the callback used in startLocationUpdates

class LocationHelper1(private val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val client: SettingsClient = LocationServices.getSettingsClient(context)

    fun checkDeviceLocationSettings(onLocationSettingsChecked: (Boolean) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            onLocationSettingsChecked(true)
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(context as Activity, REQUEST_CODE_LOCATION_SETTINGS)
                } catch (e: IntentSender.SendIntentException) {
                    // Handle exception
                    onLocationSettingsChecked(false)
                    Log.d(Constant.TAG, "Error resolving location settings", e)
                }
            } else {
                onLocationSettingsChecked(false)
            }
        }
    }

    fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermissions(onPermissionsGranted: () -> Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        ActivityCompat.requestPermissions(context as Activity, permissions, REQUEST_CODE_PERMISSIONS)

        onPermissionsGrantedCallback = onPermissionsGranted
    }

    private var onPermissionsGrantedCallback: (() -> Unit)? = null

     fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                onPermissionsGrantedCallback?.invoke()
            }
        }
    }

    fun startLocationUpdates(onLocationUpdate: (Location) -> Unit) {
        if (hasLocationPermissions() && isLocationEnabled()) {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    location?.let {

                        onLocationUpdate(location)
                    }

                }
            }

//            locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                MIN_TIME_BETWEEN_UPDATES,
//                MIN_DISTANCE_BETWEEN_UPDATES,
//                locationCallback
//            )
        } else {
            // Handle permission or location disabled cases
        }
    }

    fun stopLocationUpdates(locationCallback: LocationCallback?) {
        if (locationCallback != null) {
           // locationManager.removeUpdates(locationCallback)
        }
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    companion object {
        private const val REQUEST_CODE_LOCATION_SETTINGS = 100
        private const val REQUEST_CODE_PERMISSIONS = 101
        private const val MIN_TIME_BETWEEN_UPDATES = 10000L // 10 seconds
        private const val MIN_DISTANCE_BETWEEN_UPDATES = 500f // 500 meters
    }
}
