package com.policyboss.demoandroidapp.CameraGalleryDemo.UI

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.material.snackbar.Snackbar
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.ExtensionFun.showSnackbar
import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.databinding.ActivityImageCropperWithPermissionLauncherBinding
import com.policyboss.demoandroidapp.databinding.ActivityResultLauncherDemoBinding

class ImageCropperWithPermissionLauncherActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityImageCropperWithPermissionLauncherBinding
    lateinit var imgUri : Uri
    private lateinit var layout: View


    // region Old  Define Crop Image Contract for cropping functionality
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val croppedImageUri = result.uriContent
            val croppedImageFilePath = result.getUriFilePath(this) // Optional


            // Display the cropped image
            binding.imgProfile.setImageURI(null)
            binding.imgProfile.setImageURI(croppedImageUri)
        } else {
            val exception = result.error
            Log.e("ImageCropError", "Error during cropping: $exception")
        }
    }
    //endregion




    private val cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) startCrop(imgUri)  // Start cropping if the image was captured
    }

    private val galleryContracts = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) startCrop(uri)  // Start cropping if an image was picked
    }

    //region Permission Launcher
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted : Boolean ->

            if(isGranted){
                Log.d(Constant.TAG_Coroutine,"Permission Granted via Launcher")
                // showSnackBar(layout,"Permission Granted via Launcher")

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
    //endregion


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageCropperWithPermissionLauncherBinding.inflate(layoutInflater)  // bind data
        setContentView(binding.root)

        layout = binding.root
        layout = binding.constraintLayout


//        cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()){
//
//            // binding.imgProfile.setImageURI(null)
//            binding.imgProfile.setImageURI(imgUri)
//
//        }

//        galleryContracts = registerForActivityResult(ActivityResultContracts.GetContent()){
//
//            binding.imgProfile.setImageURI(null)
//            binding.imgProfile.setImageURI(it)
//        }

        binding.btnGallery.setOnClickListener(this)
        binding.btnCamera.setOnClickListener(this)

        binding.imgClose.setOnClickListener(this)





    }


    // Start Crop Activity with the selected or captured image URI
    private fun startCrop(imageUri: Uri) {
        cropImage.launch(
            CropImageContractOptions(
                uri = imageUri,
                cropImageOptions = CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON,  // Show guidelines during cropping
                    autoZoomEnabled = true, // Ensures the image is zoomed for better visibility
                    outputCompressFormat = Bitmap.CompressFormat.JPEG
                )
            )
        )
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

//                val permissions = arrayOf(
//                    Manifest.permission.CAMERA,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//                requestPermissionLauncher.launch(permissions) // Request both permissions together

            }

        }
    }



    override fun onClick(view: View?) {

        when(view!!.id){

            binding.imgClose.id ->{
                this.finish()
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