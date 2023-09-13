package com.policyboss.demoandroidapp.LocationDemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.policyboss.demoandroidapp.databinding.ActivityLocationDemoBinding

class LocationDemoActivity : AppCompatActivity() , LocationService.ILocation {

    lateinit var locationService: LocationService
    var btnSubmit: Button? = null
    private val REQUEST_CHECK_SETTINGS = 0x1
    private val REQUEST_CODE_ASK_PERMISSIONS = 1111

    var perms = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    lateinit var binding: ActivityLocationDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationService = LocationService(this)

        locationService.setLocationListener(this)

        if (!checkPermission()) {
            requestPermission()
        }


        binding.btnShowLocation.setOnClickListener{

            locationService.createLocationRequest()
        }

        binding.imgClose.setOnClickListener{

            this.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationService.stopLocationUpdates()
    }

    private fun checkPermission(): Boolean {
        val location = ActivityCompat.checkSelfPermission(this, perms[0])
        return location == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, perms, REQUEST_CODE_ASK_PERMISSIONS)
    }


    override fun getLocation(location: Location?) {
        if (location != null) {
            locationService.stopLocationUpdates()
            Toast.makeText(
                this,
                "Location Callback" + location.latitude + " And " + location.longitude,
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK -> {
                    Toast.makeText(this@LocationDemoActivity, "User agreed .", Toast.LENGTH_SHORT)
                        .show()
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    locationService.startLocationUpdates()
                }

                RESULT_CANCELED -> Toast.makeText(
                    this@LocationDemoActivity,
                    "User Not",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}