package com.policyboss.demoandroidapp.ViewPagerDemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.policyboss.demoandroidapp.ActivityLifecycle.ActivityLifeCycleActivity
import com.policyboss.demoandroidapp.AlertDialog.AlertDialogDemo
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.ActivityResultLauncherDemoActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.MultiplePermissionActivity
import com.policyboss.demoandroidapp.LocationBgService.LocationBackgrounDemActivity
import com.policyboss.demoandroidapp.LocationDemo.LocationDemoActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.CalanderActivity
import com.policyboss.demoandroidapp.UI.Collapsing.CollapsingToolbarLayoutActivity
import com.policyboss.demoandroidapp.UI.CropImage.CameraCropImageActivity
import com.policyboss.demoandroidapp.UI.Login.LoginActivity
import com.policyboss.demoandroidapp.UI.MaterialEditText.MaterialEditTextDemoActivity
import com.policyboss.demoandroidapp.UI.TextScanner.AutoTextReaderActivity
import com.policyboss.demoandroidapp.UI.circularProgress.CircularProgressActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.UI.ViewPagerWithProgressActivity
import com.policyboss.demoandroidapp.databinding.ActivityDashboardBinding
import com.policyboss.demoandroidapp.databinding.ActivityViewPagerMainBinding

class ViewPagerMainActivity : BaseActivity() , View.OnClickListener{

    lateinit var binding: ActivityViewPagerMainBinding

    private val callback = object : OnBackPressedCallback(enabled = true) {
        override fun handleOnBackPressed() {
            this@ViewPagerMainActivity.finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_view_pager_main)

        binding = ActivityViewPagerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "ViewPager Demo"


        }


        setClickListener()

        onBackPressedDispatcher.addCallback(this, callback)

    }

    fun setClickListener() {

        binding.btnViePager1.setOnClickListener(this)

        binding.btnViePager2.setOnClickListener(this)

        binding.btnViePager3.setOnClickListener(this)

        binding.btnViePager4.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnViePager1.id -> {


                val intent = Intent(this, ViewPagerWithProgressActivity::class.java).apply {
                    putExtra("message", "Hello, TargetActivity!")
                }

                startActivity(intent)
            }

            binding.btnViePager2.id -> {

                //startActivity(Intent(this, LocationBackgrounDemActivity::class.java))
            }

            binding.btnViePager3.id -> {

              //  startActivity(Intent(this, ViewPagerMainActivity::class.java))
            }
            binding.btnViePager4.id -> {

               // startActivity(Intent(this, AutoTextReaderActivity::class.java))

            }

        }
    }
}