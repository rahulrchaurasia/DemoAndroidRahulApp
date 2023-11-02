package com.policyboss.policybosspro.login.customSpinner

import android.os.Parcel
import android.os.Parcelable




data class SpinnerItem( val id: Int,val name: String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpinnerItem> {
        override fun createFromParcel(parcel: Parcel): SpinnerItem {
            return SpinnerItem(parcel)
        }

        override fun newArray(size: Int): Array<SpinnerItem?> {
            return arrayOfNulls(size)
        }
    }
}




