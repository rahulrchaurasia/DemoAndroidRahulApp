package com.policyboss.demoandroidapp.ActivityLifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.policyboss.demoandroidapp.AlertDialog.customActivityAsDialog
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.TAG
import com.policyboss.demoandroidapp.Utility.NotificationHelper
import com.policyboss.demoandroidapp.databinding.ActivityLifeCycleBinding

class ActivityLifeCycleActivity : BaseActivity() , View.OnClickListener{

    lateinit var binding: ActivityLifeCycleBinding
    lateinit var txtMessage: TextView
    lateinit var btnNext: Button
    lateinit var btnSubmit:Button
    lateinit var etName: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate invoked -First")
        setContentView(R.layout.activity_life_cycle)
        binding = ActivityLifeCycleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Alert Dialog Demo"

        }
        init()
        setOnClickListener()
        NotificationHelper.initialize(this)
    }

    // region Method

    private fun init() {
        btnNext =  binding.btnNext
        btnSubmit = binding.btnSubmit
        txtMessage = binding.txtMessage
        etName =  binding.etName
    }

    private fun setOnClickListener() {
        btnNext.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        binding.btnFragment.setOnClickListener(this)
    }

    private fun showCamerGalleryPopUp() {
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        val lyCamera: LinearLayout
        val lyGallery: LinearLayout
        var lyPdf: LinearLayout
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.layout_cam_gallery, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        // set the custom dialog components - text, image and button
        lyCamera = dialogView.findViewById<View>(R.id.lyCamera) as LinearLayout
        lyGallery = dialogView.findViewById<View>(R.id.lyGallery) as LinearLayout
        lyCamera.setOnClickListener {
            //launchCamera();
            //    alertDialog.dismiss();
        }
        lyGallery.setOnClickListener {
            // openGallery();
            //   alertDialog.dismiss();
        }
        alertDialog.setCancelable(true)
        alertDialog.show()
        //  alertDialog.getWindow().setLayout(900, 600);

        // for user define height and width..
    }
    //endregion


    //region Life Cycle Method
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart invoked-First")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume invoked-First")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause invoked-First")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop invoked-First")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart invoked-First")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy invoked-First")
    }

    //endregion

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.demo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_camera -> showCamerGalleryPopUp()

            R.id.action_dialog -> {

                startActivity(Intent(this@ActivityLifeCycleActivity, customActivityAsDialog::class.java))

            }
            R.id.action_second ->

              // intialize it on Oncreate
            NotificationHelper.showNotification(
                context = this,
                title = "Your Notification Title",
                body = "Your Notification Body"
            )

        }
        return super.onOptionsItemSelected(item)
    }

//ActivityAsDialog
    override fun onClick(view: View?) {

        when(view?.id) {

            btnSubmit.id ->{

                if (!etName.text.toString().trim { it <= ' ' }.isEmpty()) {
                    txtMessage.text = etName.text.toString()
                } else {
                    Toast.makeText(this, "Please Enter text", Toast.LENGTH_SHORT).show()
                }
            }

            btnNext.id ->{

                startActivity(
                    Intent(
                        this,
                        DemoLifeCycleSecond::class.java
                    ).putExtra(Constant.DEMO_MESSAGE, etName.text.toString())
                )

            }

            binding.btnFragment.id ->{

                startActivity(
                    Intent(
                        this,
                        FragmentDemoLifeCycleActivity::class.java
                    )
                )
            }

           // FragmentDemoLifeCycleActivity
        }

    }
}