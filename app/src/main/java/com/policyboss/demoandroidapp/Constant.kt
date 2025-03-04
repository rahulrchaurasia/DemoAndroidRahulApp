package com.policyboss.demoandroidapp

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


var TAG = "DEMOAPP"


object Constant {

    const val BASE_URL = "https://quotable.io/"

    val TAG_HILT : String = "HiltDEMO"

    val TAG_KOTLIN : String = "KotlinDEMO"

    val TAG : String = "DEMOAPP"

    val TAG_WEBVIEW : String = "WebViewDEMO"

    val TAG_WORKER : String = "WorkerDEMO"

    val TAG_SCANNER : String = "ScannerDEMO"

    const val KEY_COUNT_VALUE = "key_count"

    const val KEY_COUNT_VALUE1 = "key_count1"

     val KEY_DATA : String = "keydata"

    val KEY_DETECT_TEXT : String = "keydetectText"

    val ErrorMessage : String = "Error Occoured"

    val ErrorDefault : String = "Unknown Error"

    val NetworkError : String = "No Inerenet Connection!!"

    val PUSH_BROADCAST_ACTION : String = "com.policyboss.demoandroidapp.callDialog"

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 111

    val REQUEST_ID_POST_NOTIFICATION = 112

    val bundl_name_key = "bundl_name_key"

       val TAG_Coroutine : String = "COROUTINE"

    var DEMO_MESSAGE = "demo_message"

    const val   NOTIFICATION_EXTRA = "NOTIFICATION_EXTRA"
    const val   NOTIFICATION_PROGRESS = "NOTIFICATION_PROGRESS"
    const val   NOTIFICATION_MAX = "NOTIFICATION_MAX"
    const val   NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE"

    const val PUSH_NOTITIFICATION = "demoAndroidApp_notification"
    const val   NOTIFICATION_RECEIVERNAME = "receiverName"

    const val EmptyResponse = "Empty response body"
    const val StatusMessage =  "Network request failed with status"



    const val today = "Today"
    const val yesterday = "Yesterday"
    const val thisWeek = "This Week"
    const val lastWeek = "Last Week"
    const val thisMonth = "This Month"
    const val lastMonth = "Last Month"


    fun hideKeyBoard(view: View?, context: Context) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}