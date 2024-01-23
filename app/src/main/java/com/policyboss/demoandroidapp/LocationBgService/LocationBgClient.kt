package com.policyboss.demoandroidapp.LocationBgService

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationBgClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()
}