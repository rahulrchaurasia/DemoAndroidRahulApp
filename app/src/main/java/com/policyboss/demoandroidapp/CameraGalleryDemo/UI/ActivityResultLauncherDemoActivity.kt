package com.policyboss.demoandroidapp.CameraGalleryDemo.UI

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.material.snackbar.Snackbar
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.Utility.ExtensionFun.showSnackbar
import com.policyboss.demoandroidapp.databinding.ActivityResultLauncherDemoBinding

class ActivityResultLauncherDemoActivity : BaseActivity(), View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    lateinit var binding: ActivityResultLauncherDemoBinding
    lateinit var imgUri : Uri
    private lateinit var layout: View

    lateinit var result : ActivityResultLauncher<Intent>    // For Passing Data From Second to First Activity

    lateinit var cameraContracts : ActivityResultLauncher<Uri>   // Open Camera Using Uri

    lateinit var galleryContracts : ActivityResultLauncher<String>


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted : Boolean ->

            if(isGranted){
                Log.d(Constant.TAG_Coroutine,"Permission Granted via Launcher")
                showSnackBar(layout,"Permission Granted via Launcher")

                imgUri = Utility.createImageUri(this)
                cameraContracts.launch(imgUri)

            }else{
                Log.d(Constant.TAG_Coroutine,"Permission Denied via Launcher")
                layout.showSnackbar(
                    R.string.camera_permission_denied,
                    Snackbar.LENGTH_SHORT,
                    R.string.ok){

                    settingDialog()
                    //requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }




            }


        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultLauncherDemoBinding.inflate(layoutInflater)  // bind data
        setContentView(binding.root)

        layout = binding.root
        layout = binding.constraintLayout

        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if(result.resultCode ==  RESULT_OK){

                val data =  result.data?.getStringExtra(Constant.KEY_DATA) ?: "No Data"

                showSnackBar(binding.textView5,data)

                binding.textView5.text = data
            }


        }

        cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()){

            // binding.imgProfile.setImageURI(null)
            binding.imgProfile.setImageURI(imgUri)

        }

        galleryContracts = registerForActivityResult(ActivityResultContracts.GetContent()){

            binding.imgProfile.setImageURI(null)
            binding.imgProfile.setImageURI(it)
        }

        binding.btnGallery.setOnClickListener(this)
        binding.btnCamera.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.imgClose.setOnClickListener(this)





    }



    fun settingDialog(){

        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(getString(R.string.permission_required))
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("OPPEN SETTING", DialogInterface.OnClickListener { dialog, id ->

                dialog.dismiss()
                openSetting()

            }).setNegativeButton("CANCEL", DialogInterface.OnClickListener{ dialog, id ->

                dialog.dismiss()

            })



        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("AlertDialogExample")
        // show alert dialog
        alert.show()
    }

    fun openSetting(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun CameraPermission(){

        when{
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

                Log.d(Constant.TAG_Coroutine,"Permission Granted via Req : Use Camera")
                showSnackBar(layout,"Permission Granted via Req : Use Camera")

                imgUri = Utility.createImageUri(this)
                cameraContracts.launch(imgUri)

            }


            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            )-> {
                // For Rationale : Again Request using launcher
                Log.d(Constant.TAG_Coroutine,"required shouldShowRequestPermissionRationale")



                val snack = Snackbar.make(layout,getString(R.string.permission_required), Snackbar.LENGTH_INDEFINITE)
                snack.setAction("Ok", View.OnClickListener {

                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                })
                snack.show()

            }

            else -> {
                // For First time Ask Permission
                Log.d(Constant.TAG_Coroutine,"Permission Not ask Yet")
                //  showSnackBar(layout,"Permission Not ask Yet")
                // Permission Is Not Ask yet Request the Permisssion

                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

        }
    }





    override fun onClick(view: View?) {

        when(view!!.id){

            binding.imgClose.id ->{
                this.finish()
            }
            binding.btnNext.id  ->{

                // Simple Format
                val intent = Intent(this, PermissionActivity::class.java)
                result.launch(intent)

                // region or Short Format
                /*
                result.launch(
                 Intent(this,SecondActivity::class.java)
                )
                */
                //endregion

            }

            binding.btnCamera.id -> {


                CameraPermission()

            }

            binding.btnGallery.id ->{

                galleryContracts.launch("image/*")


            }

        }
    }

}