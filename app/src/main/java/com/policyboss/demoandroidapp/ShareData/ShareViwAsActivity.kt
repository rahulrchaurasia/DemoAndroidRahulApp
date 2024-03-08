package com.policyboss.demoandroidapp.ShareData

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant

import com.policyboss.demoandroidapp.Utility.Utility

import com.policyboss.demoandroidapp.databinding.ActivityShareViwAsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareViwAsActivity : BaseActivity() , OnClickListener{

    lateinit var binding : ActivityShareViwAsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareViwAsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListner()
    }

    fun setOnClickListner(){

        binding.fbShare.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
        binding.fbDownload.setOnClickListener(this)
        binding.fbDownloadPdf.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.fbShare.id -> {


                shareImageViaView()
                //openSavedFile
            }
            binding.fbDownload.id -> {


                downloadImageViaView()

                //openSavedFile
            }
            binding.fbDownloadPdf.id -> {


                downloadPDFViaView()
            }

            binding.ivClose.id -> {


                this.finish()
            }
        }
    }





    fun downloadPDFViaView(){

        val mimeType : String = "image/png"
        displayLoadingWithText(binding.root,"Loading")
        lifecycleScope.launch(Dispatchers.Main) {
            // Use try-catch block for error handling
            try {
                val bitmap = async(Dispatchers.IO) { viewToBitmap(binding.clParent) }.await()


                val uri = Utility.createPdf(this@ShareViwAsActivity,bitmap, fileName = "myReportData")

                hideLoading()
                if (uri != null) {

                    toast("File Downloaded Successfully....")

                    // Open Downloaded file
//                    openSavedFile(
//
//                        context = this@ShareViwAsActivity,
//                        fileUri = uri,
//                        mimeType = mimeType
//                    )

                    //Share File
                    Utility.shareDataWithMessage(context = this@ShareViwAsActivity,
                        imageUri = uri,
                        subject = "Product Details",
                        message = "***Product Data***",
                        mimeType = "application/pdf"
                    )

                } else {
                    // Handle file saving failure
                    Toast.makeText(this@ShareViwAsActivity, "File saving failed", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Handle exceptions during bitmap conversion or file saving
                Toast.makeText(this@ShareViwAsActivity, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e(Constant.TAG, "Error in image sharing:", e)
            }
        }

    }

    fun shareImageViaView(){

        displayLoadingWithText(binding.root,"Loading...")
        lifecycleScope.launch(Dispatchers.Main) {
            // Use try-catch block for error handling
            try {
                val bitmap = async(Dispatchers.IO) { viewToBitmap(binding.clParent) }.await()

                val uri = getUriFile(bitmap)

                hideLoading()
                if (uri != null) {

                    Utility.shareDataWithMessage(context = this@ShareViwAsActivity,
                        imageUri = uri,
                        subject = "Product Details",
                        message = "***Product Data***",
                    )



                } else {
                    // Handle file saving failure
                    Toast.makeText(this@ShareViwAsActivity, "File saving failed", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Handle exceptions during bitmap conversion or file saving
                Toast.makeText(this@ShareViwAsActivity, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                Log.d(Constant.TAG, "Error in image sharing:", e)
            }
        }

    }

    fun downloadImageViaView(mimeType : String = "image/png"){

        displayLoadingWithText(binding.root,"Loading...")
        lifecycleScope.launch(Dispatchers.Main) {
            // Use try-catch block for error handling
            try {
                val bitmap = async(Dispatchers.IO) { viewToBitmap(binding.clParent) }.await()

                val uri = getUriFile(bitmap)

                hideLoading()
                if (uri != null) {


                    toast("File Downloaded Successfully....")

                    // Open Downloaded file
                    openSavedFile(

                        context = this@ShareViwAsActivity,
                        fileUri = uri,
                        mimeType = mimeType
                    )


                } else {
                    // Handle file saving failure
                    Toast.makeText(this@ShareViwAsActivity, "File saving failed", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Handle exceptions during bitmap conversion or file saving
                Toast.makeText(this@ShareViwAsActivity, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                Log.d(Constant.TAG, "Error in image sharing:", e)
            }
        }

    }


    fun openSavedFile(context: Context, fileUri: Uri, mimeType: String) {
        val openFileIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(openFileIntent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where no activity is found to handle the intent
            Toast.makeText(context, "No application found to open the file", Toast.LENGTH_SHORT).show()
        }
    }




    private suspend fun getUriFile(bitmap: Bitmap): Uri? {
        return withContext(Dispatchers.IO) {
            try {

                // val fileName = "myData${System.currentTimeMillis()}"
                val fileName = "myReport${System.currentTimeMillis()}"
                Utility.saveBitmapToUri(context = this@ShareViwAsActivity, bitmap = bitmap, filename = fileName,)
            } catch (e: Exception) {
                Log.d(Constant.TAG, "Error saving bitmap to file:", e)
                null
            }
        }
    }


    private fun viewToBitmap(view: View): Bitmap {
        val width = view.measuredWidth
        val height = view.measuredHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return bitmap
    }


}