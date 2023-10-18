package com.policyboss.demoandroidapp.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.ActivityResultLauncherDemoActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.MultiplePermissionActivity
import com.policyboss.demoandroidapp.FileUpload.FileUploadActivity
import com.policyboss.demoandroidapp.KotlinDemo.KotlinDemoActivity
import com.policyboss.demoandroidapp.LocationDemo.LocationDemoActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.Collapsing.CollapsingToolbarLayoutActivity
import com.policyboss.demoandroidapp.UI.Login.LoginActivity
import com.policyboss.demoandroidapp.databinding.ActivityDashboardBinding
import com.policyboss.demoandroidapp.databinding.ActivityFlowDemoBinding

class DashboardActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var binding : ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "DashBoard"


        }


        setClickListener()
    }

    fun setClickListener() {

        binding.btnLocationDemo.setOnClickListener(this)

        binding.btnSingleSelect.setOnClickListener(this)

        binding.btnMultiSelect.setOnClickListener(this)

        binding.btnPermission.setOnClickListener(this)

        binding.btnLauncher.setOnClickListener(this)

        binding.btnLogin.setOnClickListener(this)

        binding.btnCollapsing.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnLocationDemo.id -> {



                val intent = Intent(this, LocationDemoActivity::class.java).apply {
                    putExtra("message", "Hello, TargetActivity!")
                }

                startActivity(intent)
            }

            binding.btnSingleSelect.id -> {

                //startActivity(Intent(this, FileUploadActivity::class.java))

            }
            binding.btnMultiSelect.id -> {

                //startActivity(Intent(this, KotlinDemoActivity::class.java))

            }
            binding.btnPermission.id -> {


                startActivity(Intent(this, MultiplePermissionActivity::class.java))

            }
            binding.btnLauncher.id -> {


                startActivity(Intent(this, ActivityResultLauncherDemoActivity::class.java))
            }

            binding.btnLogin.id -> {


                startActivity(Intent(this, LoginActivity::class.java))
            }
            binding.btnCollapsing.id -> {


                startActivity(Intent(this, CollapsingToolbarLayoutActivity::class.java))
            }
        }
    }
}