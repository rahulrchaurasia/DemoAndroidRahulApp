package com.policyboss.demoandroidapp.PermissionManager

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi


// Refer :-- https://github.com/theappbusiness/android-permission-manager
sealed class Permission(vararg val permission: String) {


    object Camera : Permission(CAMERA)

    object MandatoryForFeatureOne : Permission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)

    object Location : Permission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    object Storage : Permission(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)

    @RequiresApi(Build.VERSION_CODES.S)
    object Bluetooth : Permission(BLUETOOTH_SCAN, BLUETOOTH_CONNECT)

    @RequiresApi(Build.VERSION_CODES.S)
    object Api12Group :
        Permission(
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE,
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )

    companion object {
        fun from(permission: String) = when (permission) {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
            CAMERA -> Camera
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Api12Group
            else -> throw IllegalArgumentException("Unknown permission $permission")
        }
    }
}
