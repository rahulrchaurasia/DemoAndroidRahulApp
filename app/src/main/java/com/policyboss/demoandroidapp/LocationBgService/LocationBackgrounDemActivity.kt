package com.policyboss.demoandroidapp.LocationBgService

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant

import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.ActivityLocationBackgrounDemBinding

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
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_backgroun_dem)

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

    fun setOnClickListner(){

        binding.btnStop.setOnClickListener(this)
        binding.btnStart.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
    }

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


    override fun onResume() {
        super.onResume()
        registerReceiver(locationReceiver, IntentFilter(LocationBgService.Location_UPDATE))
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
                           Intent(applicationContext, LocationBgService::class.java).apply {
                               action = LocationBgService.ACTION_START
                               startService(this)
                           }

                       }


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