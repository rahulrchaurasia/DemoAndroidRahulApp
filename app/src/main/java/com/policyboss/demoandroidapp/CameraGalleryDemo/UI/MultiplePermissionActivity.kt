package com.policyboss.demoandroidapp.CameraGalleryDemo.UI

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.Utility.showSnackbar
import com.policyboss.demoandroidapp.databinding.ActivityMultiplePermissionBinding


//https://www.youtube.com/watch?v=xsUnbQEfJ6I

//https://medium.com/@ajinkya.kolkhede1/requesting-runtime-permissions-using-new-activityresult-api-cb6116551f00
//https://gist.github.com/SurajBahadur/671521c379502495d9ef0f6f1dc21724           [ Multiple Permission]

class MultiplePermissionActivity : BaseActivity() {
    private lateinit var binding: ActivityMultiplePermissionBinding
    lateinit var layout: View
    lateinit var imgUri: Uri

    lateinit var cameraContracts: ActivityResultLauncher<Uri>   // Open Camera Using Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplePermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layout = binding.root

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.apply {

            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setTitle("Multiple Permission")
        }

        cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()) {

            binding.imgProfile.setImageURI(imgUri)
        }

        binding.btnCamera.setOnClickListener {

            takePhoto()
        }

    }

    private fun takePhoto() {

        when {
            //Check if permission given already Take action
            (hasCamerPermission() == PackageManager.PERMISSION_GRANTED
                    && hasReadStoragePermission() == PackageManager.PERMISSION_GRANTED) -> {

                invokeCamera()
            }

            //Check if permission not given check for Rationale means : if permission is
            // deny condition than give message for requesting permission
            (hasCamerRationalPermission() || hasReadStorageRationalPermission()) -> {

                layout.showSnackbar(
                    R.string.permission_required,
                    Snackbar.LENGTH_INDEFINITE,
                    R.string.ok
                )
                {

                    requestMultiplePermission()
                }


            }
            else -> {
                // For First time Ask Permission
                Log.d(Constant.TAG_Coroutine,"Permission Not ask Yet")
                requestMultiplePermission()

            }


        }

    }



    private fun invokeCamera() {


        Log.d(Constant.TAG_Coroutine, "Camera Open")
        imgUri = Utility.createImageUri(this)
        cameraContracts.launch(imgUri)

    }

    private val requestMultiplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

                permission ->

            var permissionGranted = false

//            permission.forEach {
//
//                if (it.value == true) {
//                    permissionGranted = true
//                } else {
//                    permissionGranted = false
//                    return@forEach
//                    //breaking@forEach
//
//                }
//            }

            var read_storage = permission[Manifest.permission.READ_EXTERNAL_STORAGE ] ?: false
            var camera = permission[Manifest.permission.CAMERA ] ?: false
            if (read_storage && camera) {
                invokeCamera()
            } else {

                layout.showSnackbar(
                    R.string.permission_required,
                    Snackbar.LENGTH_LONG,
                    R.string.ok
                )
                {

                    settingDialog()

                }



            }

        }


    fun settingDialog() = showAlert(
        "Permission",
        getString(R.string.cam_permission)
    ) { strType: String, dialog: DialogInterface ->

        when (strType) {

            "Y" -> {
                dialog.dismiss()
                Utility.openSetting(this)
            }
            "N" -> {
                dialog.dismiss()
            }
        }
    }




    fun requestMultiplePermission() = requestMultiplePermissionLauncher.launch(
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    fun hasCamerPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

    fun hasWriteStoragePermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun hasReadStoragePermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

    fun hasCamerRationalPermission() =
        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)

    fun hasWrireStorageRationalPermission() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    fun hasReadStorageRationalPermission() = ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )



}