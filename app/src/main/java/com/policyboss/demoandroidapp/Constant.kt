package com.policyboss.demoandroidapp

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


var TAG = "DEMOAPP"


object Constant {

    val TAG_HILT : String = "HiltDEMO"

    val TAG_KOTLIN : String = "KotlinDEMO"

    val TAG : String = "DEMOAPP"

    val TAG_WEBVIEW : String = "WebViewDEMO"

    val TAG_WORKER : String = "WorkerDEMO"

    val TAG_SCANNER : String = "ScannerDEMO"

    const val KEY_COUNT_VALUE = "key_count"

    const val KEY_COUNT_VALUE1 = "key_count1"

     val KEY_DATA : String = "keydata"

    val ErrorMessage : String = "Error Occoured"

    val ErrorDefault : String = "Unknown Error"

    val NetworkError : String = "No Inerenet Connection!!"

    val PUSH_BROADCAST_ACTION : String = "com.policyboss.demoandroidapp.callDialog"

    val REQUEST_ID_MULTIPLE_PERMISSIONS = 111

    val bundl_name_key = "bundl_name_key"

       val TAG_Coroutine : String = "COROUTINE"

    var DEMO_MESSAGE = "demo_message"


    fun hideKeyBoard(view: View?, context: Context) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}