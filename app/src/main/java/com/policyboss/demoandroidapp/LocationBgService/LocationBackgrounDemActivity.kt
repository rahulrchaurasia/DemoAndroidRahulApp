package com.policyboss.demoandroidapp.LocationBgService

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.policyboss.demoandroidapp.BaseActivity

import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityLocationBackgrounDemBinding

class LocationBackgrounDemActivity : BaseActivity() , View.OnClickListener {

    lateinit var binding : ActivityLocationBackgrounDemBinding
    private val MY_LOCATION_PERMISSION_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_backgroun_dem)

        binding = ActivityLocationBackgrounDemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListner()

        if (hasLocationPermissions()) {
            // Permission granted, proceed with location access
        } else {
            // Permission not granted, request it
            requestLocationPermissions()
        }
    }

    fun setOnClickListner(){

        binding.btnStop.setOnClickListener(this)
        binding.btnStart.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
    }

    fun hasLocationPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                )


    }

    fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnStart.id -> {

               // shareImageViaView()
                Intent(applicationContext, LocationBgService::class.java).apply {
                    action = LocationBgService.ACTION_START
                    startService(this)
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