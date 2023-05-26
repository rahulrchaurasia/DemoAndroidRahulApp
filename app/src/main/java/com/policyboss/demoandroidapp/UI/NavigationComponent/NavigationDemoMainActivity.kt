package com.policyboss.demoandroidapp.UI.NavigationComponent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.TAG
import com.policyboss.demoandroidapp.UI.NavigationComponent.BasicDemo.NavigationBasicActivity
import com.policyboss.demoandroidapp.UI.NavigationComponent.advanceDemo.activity.NavigationAdvanceActivity
import com.policyboss.demoandroidapp.databinding.ActivityNavigationDemoBinding

class NavigationDemoMainActivity : AppCompatActivity()  , View.OnClickListener {

    lateinit var binding: ActivityNavigationDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate invoked")
        binding = ActivityNavigationDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Navigation Component"
        }

        setOnClickListener()
    }

    fun setOnClickListener(){

        binding.btnBasicDemo.setOnClickListener(this)
        binding.btnDemoAdvance.setOnClickListener(this)
    }


    //region Life Cycle Method

    //region Life Cycle Method
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart invoked")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume invoked")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause invoked")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop invoked")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart invoked")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy invoked")
    }

    //endregion

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.demo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_camera -> showCamerGalleryPopUp()

           R.id.action_second ->  startActivity(Intent(this, NavigationBasicActivity::class.java))


        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCamerGalleryPopUp() {
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        val lyCamera: LinearLayout
        val lyGallery: LinearLayout
        var lyPdf: LinearLayout
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.layout_cam_gallery, null)
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
    override fun onClick(view: View?) {


        when(view?.id){

            binding.btnBasicDemo.id -> {

                startActivity(Intent(this, NavigationBasicActivity::class.java))

            }

            binding.btnDemoAdvance.id -> {

                startActivity(Intent(this, NavigationAdvanceActivity::class.java))

            }
        }
    }
}