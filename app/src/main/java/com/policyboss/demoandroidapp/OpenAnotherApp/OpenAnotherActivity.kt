package com.policyboss.demoandroidapp.OpenAnotherApp

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.UtilityNew.showAlert
import com.policyboss.demoandroidapp.databinding.ActivityOpenAnotherBinding

class OpenAnotherActivity : BaseActivity() {

    lateinit var binding : ActivityOpenAnotherBinding
    lateinit var layout: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenAnotherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layout = binding.root
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {

            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setTitle("Open Another App")

        }

        binding.btnSubmit.setOnClickListener {


            openExternalApp()
            //  startActivity(Intent(this, CallerDialogActivity::class.java))

        }


    }

    private fun openExternalApp(){

        var packageName1 = "com.utility.finmartcontact"  //"com.utility.finmartcontact/.login.LoginActivity"
        var packageName = "com.policyboss.policybosspro"  //"com.google.android.youtube"
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName1)
        if (launchIntent != null) {
            startActivity(launchIntent.putExtra("fbaid","1976",)
                .putExtra("ssid","15921")
                .putExtra("parentid","0")
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } else {

            showAlert("There is no package available in android")
        }

    }

    fun canDrawOverlays(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) true else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            Settings.canDrawOverlays(context)
        } else {
            if (Settings.canDrawOverlays(context)) return true
            try {
                val mgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    ?: return false
                //getSystemService might return null
                val viewToAdd = View(context)
                val params = WindowManager.LayoutParams(
                    0,
                    0,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT
                )
                viewToAdd.layoutParams = params
                mgr.addView(viewToAdd, params)
                mgr.removeView(viewToAdd)
                return true
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            false
        }
    }
}