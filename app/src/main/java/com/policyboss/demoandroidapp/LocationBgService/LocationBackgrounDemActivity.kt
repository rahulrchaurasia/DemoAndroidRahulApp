package com.policyboss.demoandroidapp.LocationBgService

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant

import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.ActivityLocationBackgrounDemBinding
import java.io.IOException
import java.util.Locale

class LocationBackgrounDemActivity : BaseActivity() , View.OnClickListener {

    lateinit var binding : ActivityLocationBackgrounDemBinding
    private val MY_LOCATION_PERMISSION_REQUEST_CODE = 101

    private lateinit var locationHelper: LocationHelper

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val lat = intent.getStringExtra("lat")
            val lon = intent.getStringExtra("lon")
            // Update your TextView here
            binding.txtLat.text = "$lat"
            binding.txtLon.text = "$lon"

            getAddressFromLatLon(latitude = lat?:"", longitude = lon?:"")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_location_backgroun_dem)

        binding = ActivityLocationBackgrounDemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListner()

        locationHelper = LocationHelper(context = applicationContext, activity = this@LocationBackgrounDemActivity)


        if (hasLocationPermissions()) {
            // Permission granted, proceed with location access
            locationHelper.checkDeviceLocationSettings { isSettingsEnabled ->

                Log.d(Constant.TAG,"Location is Enable ${isSettingsEnabled}")

            }
        } else {
            // Permission not granted, request it
            requestLocationPermissions()
        }
        // For Enable Location Setting


    }

    fun getAddressFromLatLon( latitude: String = "", longitude: String = "") {

        if(latitude.isEmpty() || longitude.isEmpty()){

            return
        }
        val geocoder = Geocoder(applicationContext, Locale.getDefault())

         try {
            val addresses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1) // Get up to 1 address
           // val addressList: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            addresses?.firstOrNull()?.let { address ->
                // Build a comprehensive address string
                val addressLine = address.getAddressLine(0)
                val locality = address.locality
                val adminArea = address.adminArea
                val countryName = address.countryName
                "$addressLine, $locality, $adminArea, $countryName"


                binding.txtAddress.setText(" $addressLine, $locality, $adminArea")

            }
        } catch (e: IOException) {
            // Handle potential network or service errors gracefully
            Log.e(Constant.TAG, "Error getting address", e)
            Log.d(Constant.TAG, "Error getting address", e)
            null // Return null to indicate error
        }
    }

    fun setOnClickListner(){

        binding.btnStop.setOnClickListener(this)
        binding.btnStart.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
    }

    //region permissioin
    fun hasLocationPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                )


    }

    fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS),
            MY_LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    //endregion


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
       //

         //For BroadCast Handling
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(locationReceiver, IntentFilter(LocationBgService.Location_UPDATE), RECEIVER_EXPORTED)
        }else {
            registerReceiver(locationReceiver, IntentFilter(LocationBgService.Location_UPDATE))
        }



    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(locationReceiver)
    }
    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnStart.id -> {

                locationHelper.checkDeviceLocationSettings { isSettingsEnabled ->

                    if (isSettingsEnabled) {
                        // Location settings are good, proceed with location-related tasks
                       if(locationHelper.hasLocationPermissions()) {



                           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                               // Use startForegroundService() for devices running Oreo (API level 26) or higher
                               Intent(applicationContext, LocationBgService::class.java).apply {
                                   action = LocationBgService.ACTION_START
                                   startForegroundService(this)
                               }
                           } else {
                               // Use startService() for devices running older Android versions
                               Intent(applicationContext, LocationBgService::class.java).apply {
                                   action = LocationBgService.ACTION_START
                                   startService(this)
                               }
                           }



                       }


                    }

                    else{
                        Utility.showSettingsAlert(context = this@LocationBackgrounDemActivity)
                    }
                }

            }

            binding.btnStop.id -> {


                Intent(applicationContext, LocationBgService::class.java).apply {
                    action = LocationBgService.ACTION_STOP
                    startService(this)
                }
            }
            binding.ivClose.id -> {


                this.finish()
            }
        }
    }
}