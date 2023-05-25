package com.policyboss.demoandroidapp.Utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Created by Rahul on 16/06/2022.
 */

class NetworkUtils {

    companion object{



       open fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }


        open  fun isNetworkAvailable1(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    //It will check for both wifi and cellular network
                    return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI)
                }
                return false
            } else {
                @Suppress("DEPRICATION")
                val netInfo = cm.activeNetworkInfo
                return netInfo != null && netInfo.isConnectedOrConnecting
            }
        }


    }
}