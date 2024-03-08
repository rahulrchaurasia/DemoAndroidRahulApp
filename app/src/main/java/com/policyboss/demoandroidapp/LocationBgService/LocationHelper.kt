package com.policyboss.demoandroidapp.LocationBgService

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.policyboss.demoandroidapp.LocationDemo.LocationService

// Start location updates:
//locationHelper.startLocationUpdates { location ->
//    // Use the received location object for your purposes
//}
//
//locationHelper.stopLocationUpdates(locationCallback) // Pass the callback used in startLocationUpdates

class LocationHelper(private val context: Context, private val activity: Activity) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val client: SettingsClient = LocationServices.getSettingsClient(context)
    val REQUEST_CHECK_SETTINGS = 0x1
    fun checkDeviceLocationSettings(onLocationSettingsChecked: (Boolean) -> Unit) {

        //region depricated
//        val locationRequest = LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 5000
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
        //endregion

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            // Set the desired update interval in milliseconds (e.g., 300000 for 5 minutes)
            300000
        )
            .setMinUpdateDistanceMeters(10f) // Set the minimum distance between updates (optional)
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL) // Use permission-level granularity
            .setWaitForAccurateLocation(true) // Wait for a more accurate location (optional)
            .build()


        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val task = client.checkLocationSettings(settingsRequest)

        task.addOnSuccessListener {
            onLocationSettingsChecked(true)
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {

                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    onLocationSettingsChecked(false)
                    val resolvable = exception as ResolvableApiException
                    resolvable.startResolutionForResult(
                        activity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: SendIntentException) {
                    // Ignore the error.
                    onLocationSettingsChecked(false)
                }
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
