package com.policyboss.demoandroidapp.CameraGalleryDemo.UI

import PDFHandlerAdv
import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions

import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.material.snackbar.Snackbar
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.ExtensionFun.showSnackbar
import com.policyboss.demoandroidapp.Utility.PdfHandler.PDFHandler

import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.databinding.ActivityImageCropperWithPermissionLauncherBinding
import kotlinx.coroutines.launch
import java.io.File

/*
//Note : com.vanniktech:android-image-cropper:4.6.0
  canhub Image Crooper
 */

class ImageCropperWithPermissionLauncherActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityImageCropperWithPermissionLauncherBinding
    lateinit var imgUri : Uri
    private lateinit var layout: View

    // region Pdf File Decleration
    private val pdfHandler = PDFHandlerAdv()
    private val PDF_PICKER_CODE = 123
    //endregion


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

        binding.btnFile.setOnClickListener(this)
        binding.imgClose.setOnClickListener(this)





    }



    private fun openPDFPicker() {
        pdfHandler.launchPDFPicker(this, PDF_PICKER_CODE)
    }
    //region Start Crop Activity with the selected or captured image URI
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

     //endregion


    //region Permission and Open Setting Page
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
    //endregion



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

            binding.btnFile.id -> {

                openPDFPicker()


            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PDF_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                lifecycleScope.launch {
                    try {
                        val cachedFile = pdfHandler.handlePDFResult(this@ImageCropperWithPermissionLauncherActivity, uri)
                        if (cachedFile != null) {
                            // Log success details
                            showAlert("Pdf cached successfully ${cachedFile.absolutePath}")
                            // File is successfully cached
                            Log.d(
                                "PDFCache",
                                "PDF cached successfully at: ${cachedFile.absolutePath}"
                            )
                            Log.d("PDFHandler", "Successfully cached file: ${cachedFile.absolutePath}")
                            Log.d("PDFHandler", "File size: ${cachedFile.length()} bytes")

                            // Process the file
                            handleCachedFile(cachedFile)
                        } else {
                            
                            showAlert("Failed to process PDF ${cachedFile?.absolutePath}")
                            Log.d(
                                "PDFCache",
                                "PDF not generated:"
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("PDFHandler", "Error: ${e.message}", e)
                        Toast.makeText(this@ImageCropperWithPermissionLauncherActivity,
                            "Error processing file: ${e.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun handleCachedFile(file: File) {
        // Process your cached file here
        // For example, upload to server
        lifecycleScope.launch {
            try {
                val uploaded = pdfHandler.uploadPDFToServer(file, "YOUR_SERVER_URL")
                if (uploaded) {
                    Toast.makeText(this@ImageCropperWithPermissionLauncherActivity,
                        "File uploaded successfully",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ImageCropperWithPermissionLauncherActivity,
                        "Upload failed",
                        Toast.LENGTH_SHORT).show()
                }
            } finally {
                // Optionally clear cache after processing
                pdfHandler.clearCache(this@ImageCropperWithPermissionLauncherActivity)
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PDF_PICKER_CODE && resultCode == Activity.RESULT_OK) {
//            data?.data?.let { uri ->
//                try {
//                    // Take persistent permission
//                    contentResolver.takePersistableUriPermission(
//                        uri,
//                        Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    )
//
//                    lifecycleScope.launch {
//                        try {
//                            // Cache the PDF
//                            val cachedFile = pdfHandler.handlePDFResult(this@ImageCropperWithPermissionLauncherActivity, uri)
//
//
//                            if (cachedFile != null) {
//
//
//                                showAlert("Pdf cached successfully ${cachedFile.absolutePath}")
//                                // File is successfully cached
//                                Log.d(
//                                    "PDFCache",
//                                    "PDF cached successfully at: ${cachedFile.absolutePath}"
//                                )
//                                // Log cache information
//                                Log.d("PDFCache", "Cache size: ${pdfHandler.getCacheSize(this@ImageCropperWithPermissionLauncherActivity)}")
//                                Log.d("PDFCache", "File path: ${cachedFile.absolutePath}")
//                                Log.d("PDFCache", "File size: ${cachedFile.length()}")
//
//
//                                // Upload to server
//                                val uploaded = pdfHandler.uploadPDFToServer(cachedFile, "YOUR_SERVER_URL")
//                                if (uploaded) {
//                                    Toast.makeText(this@ImageCropperWithPermissionLauncherActivity, "PDF uploaded successfully", Toast.LENGTH_SHORT).show()
//                                    // Optionally clear cache after successful upload
//                                    pdfHandler.clearCache(this@ImageCropperWithPermissionLauncherActivity)
//                                } else {
//                                    Toast.makeText(this@ImageCropperWithPermissionLauncherActivity, "Upload failed", Toast.LENGTH_SHORT).show()
//                                }
//                            } else {
//
//                                showAlert("Failed to process PDF ${cachedFile?.absolutePath}")
//                                Log.d(
//                                    "PDFCache",
//                                    "PDF not generated:"
//                                )
//                            }
//                        } catch (e: Exception) {
//                            Log.e("ImageCropperWithPermissionLauncherActivity", "Error: ${e.message}")
//                            Toast.makeText(this@ImageCropperWithPermissionLauncherActivity, "Error processing PDF", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } catch (e: SecurityException) {
//                    Log.e("ImageCropperWithPermissionLauncherActivity", "Permission error: ${e.message}")
//                    Toast.makeText(this, "Cannot access the selected file", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//

}