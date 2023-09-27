package com.policyboss.demoandroidapp.CameraGalleryDemo.UI

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.Utility.showSnackbar
import com.policyboss.demoandroidapp.databinding.ActivitySecondBinding

class PermissionActivity : BaseActivity() {
    lateinit var binding: ActivitySecondBinding
    private lateinit var layout: View
    lateinit var imgUri : Uri

    // CAMERA  ////////
    lateinit var cameraContracts : ActivityResultLauncher<Uri>   // Open Camera Using Uri

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission has been granted. Start camera preview Activity.
                Log.d(Constant.TAG_Coroutine,"Permission Granted via Launcher")
                showSnackBar(layout,"Permission Granted via Launcher")

                startCamera()      // Camera Start Using Camera Launcher

            } else {
                // Permission request was denied.
                Log.d(Constant.TAG_Coroutine,"Permission Denied via Launcher")
                showSnackBar(layout,"Permission Denied via Launcher")

                settingDialog()      // Open setting Alert Dialog
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = binding.root


        // CAMERA ////////

        //region camera result

        cameraContracts = registerForActivityResult(ActivityResultContracts.TakePicture()){

            // binding.imgProfile.setImageURI(null)
            binding.imgProfile.setImageURI(imgUri)

        }

        //endregion

        binding.btnClose.setOnClickListener {

            setResult(RESULT_OK, Intent().putExtra(Constant.KEY_DATA,"test Data"))
            finish()
        }

        binding.btnCamera.setOnClickListener {

            showCameraDialog()
        }
    }



    // region Camera Opening
    @RequiresApi(Build.VERSION_CODES.M)
    private fun showCameraDialog() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {


            Log.d(Constant.TAG_Coroutine,"Permission Granted via Req : Use Camera")
            showSnackBar(layout,"Permission Granted via Req : Use Camera")

            startCamera()

        } else {
            requestCameraPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            layout.showSnackbar(
                R.string.permission_required,
                Snackbar.LENGTH_INDEFINITE,
                R.string.ok
            ) {

                //  For Rationale :  Again Request using launcher
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        } else {
            // For First time Ask Permission
            // You can directly ask for the permission.
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        }
    }

    private fun startCamera() {

        imgUri = Utility.createImageUri(this)
        cameraContracts.launch(imgUri)            // Camera Start Using Camera Launcher
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
                Utility.openSetting(this)

            }).setNegativeButton("CANCEL", DialogInterface.OnClickListener{ dialog, id ->

                dialog.dismiss()

            })



        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Permission")
        // show alert dialog
        alert.show()
    }

    //endregion

    override fun onBackPressed() {
        // super.onBackPressed()
        setResult(RESULT_OK, Intent().putExtra(Constant.KEY_DATA,"test Data"))
        finish()
    }
}