package com.policyboss.demoandroidapp.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.policyboss.demoandroidapp.ActivityLifecycle.ActivityLifeCycleActivity
import com.policyboss.demoandroidapp.AlertDialog.AlertDialogDemo
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.ActivityResultLauncherDemoActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.ImageCropperWithPermissionLauncherActivity
import com.policyboss.demoandroidapp.CameraGalleryDemo.UI.MultiplePermissionActivity
import com.policyboss.demoandroidapp.LocationBgService.LocationBackgrounDemActivity
import com.policyboss.demoandroidapp.LocationDemo.LocationDemoActivity
import com.policyboss.demoandroidapp.LoginModule.LoginActivityMain
import com.policyboss.demoandroidapp.UI.Collapsing.CollapsingToolbarLayoutActivity
import com.policyboss.demoandroidapp.UI.Collapsing.CollapsingToolbarMain
import com.policyboss.demoandroidapp.UI.CropImage.CameraCropImageActivity
import com.policyboss.demoandroidapp.UI.Login.LoginActivity
import com.policyboss.demoandroidapp.UI.MaterialEditText.MaterialEditTextDemoActivity
import com.policyboss.demoandroidapp.UI.RecyclerViewOperation.RecyclerViewMainActivity
import com.policyboss.demoandroidapp.UI.TextScanner.AutoTextReaderActivity
import com.policyboss.demoandroidapp.UI.circularProgress.CircularProgressActivity

import com.policyboss.demoandroidapp.Utility.PopupViewTop
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerMainActivity
import com.policyboss.demoandroidapp.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : BaseActivity() , View.OnClickListener{

    lateinit var binding : ActivityDashboardBinding

//    private val callback = object  : OnBackPressedCallback(enabled = true){
//        override fun handleOnBackPressed() {
//            this@DashboardActivity.finish()
//        }
//
//    }

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

        //BACKPRRESS EVENT
      //  onBackPressedDispatcher.addCallback(this, callback)

    }

    fun setClickListener() {

        binding.btnLocationDemo.setOnClickListener(this)

        binding.btnAutoTextReader.setOnClickListener(this)

        binding.btnCropImage.setOnClickListener(this)

        binding.btnPermission.setOnClickListener(this)

        binding.btnLauncher.setOnClickListener(this)

        binding.btnImagCrooper.setOnClickListener(this)

        binding.btnLogin.setOnClickListener(this)

        binding.btnLogin1.setOnClickListener(this)

        binding.btnCollapsing.setOnClickListener(this)

        binding.btnCalander.setOnClickListener(this)

        binding.btnAlertDemo.setOnClickListener(this)

        binding.btnActivityLifecycle.setOnClickListener(this)

        binding.btnLocatinInBg.setOnClickListener(this)

        binding.btnEditTextDemo.setOnClickListener(this)

        binding.btnCircularProgress.setOnClickListener(this)

        binding.btnViewPagerMain.setOnClickListener(this)

        binding.btnRecycleViewMain.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnLocationDemo.id -> {



                val intent = Intent(this, LocationDemoActivity::class.java).apply {
                    putExtra("message", "Hello, TargetActivity!")
                }

                startActivity(intent)
            }

            binding.btnLocatinInBg.id -> {

                startActivity(Intent(this, LocationBackgrounDemActivity::class.java))
            }

            binding.btnViewPagerMain.id -> {

                startActivity(Intent(this, ViewPagerMainActivity::class.java))


            }
            binding.btnRecycleViewMain.id -> {

                startActivity(Intent(this, RecyclerViewMainActivity::class.java))


            }
            binding.btnAutoTextReader.id -> {

                startActivity(Intent(this, AutoTextReaderActivity::class.java))

            }
            binding.btnCropImage.id -> {

                //startActivity(Intent(this, KotlinDemoActivity::class.java))

                startActivity(Intent(this, CameraCropImageActivity::class.java))

            }
            binding.btnPermission.id -> {


                startActivity(Intent(this, MultiplePermissionActivity::class.java))

            }
            binding.btnLauncher.id -> {


                startActivity(Intent(this, ActivityResultLauncherDemoActivity::class.java))
            }

            binding.btnImagCrooper.id -> {


                startActivity(Intent(this, ImageCropperWithPermissionLauncherActivity::class.java))
            }

            binding.btnLogin.id -> {


                startActivity(Intent(this, LoginActivityMain::class.java))
            }
            binding.btnLogin1.id -> {


                startActivity(Intent(this, LoginActivity::class.java))
            }
            binding.btnCollapsing.id -> {


                startActivity(Intent(this, CollapsingToolbarMain::class.java))
            }
            binding.btnCalander.id -> {


                startActivity(Intent(this, CalanderActivity::class.java))
            }
            binding.btnAlertDemo.id ->{



                startActivity(Intent(this, AlertDialogDemo::class.java))


            }
            binding.btnEditTextDemo.id -> {


                startActivity(Intent(this, MaterialEditTextDemoActivity::class.java))
            }

            binding.btnCircularProgress.id -> {


                startActivity(Intent(this, CircularProgressActivity::class.java))
            }
            binding.btnActivityLifecycle.id ->{


                startActivity(Intent(this, ActivityLifeCycleActivity::class.java))


            }

        }
    }
}