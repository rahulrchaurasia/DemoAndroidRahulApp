package com.policyboss.demoandroidapp.UI.NavigationComponent

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PushNotificationEntity(
    val message: String,
    val header: String,
    val amount: Double,
    val logourl: String

) : Parcelable