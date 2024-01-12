package com.policyboss.demoandroidapp.ShareData

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.LocationDemo.LocationDemoActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.shareDara
import com.policyboss.demoandroidapp.Utility.Utility

import com.policyboss.demoandroidapp.databinding.ActivityDashboardBinding
import com.policyboss.demoandroidapp.databinding.ActivityShareViwAsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShareViwAsActivity : BaseActivity() , OnClickListener{

    lateinit var binding : ActivityShareViwAsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_viw_as)

        binding = ActivityShareViwAsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListner()
    }

    fun setOnClickListner(){

        binding.fbShare.setOnClickListener(this)
        binding.ivClose.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.fbShare.id -> {

                shareImageViaView()

            }

            binding.ivClose.id -> {


               this.finish()
            }
        }
    }




    fun shareImageViaView(){


        lifecycleScope.launch(Dispatchers.Main) {
            // Use try-catch block for error handling
            try {
                val bitmap = async(Dispatchers.IO) { viewToBitmap(binding.clParent) }.await()
                val uri = getUriFile(bitmap)

                if (uri != null) {
                   // shareDara(this@ShareViwAsActivity, fileUri = uri)
                   // shareUriToGmail(this@ShareViwAsActivity, uri = uri)
                    Utility.shareImageToGmail(context = this@ShareViwAsActivity,
                        imageUri = uri)

                 //   Utility.shareDara(this@ShareViwAsActivity, fileUri = uri)
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



    private suspend fun getUriFile(bitmap: Bitmap): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                Utility.saveBitmapToUri(context = this@ShareViwAsActivity, bitmap = bitmap, filename = "myData${System.currentTimeMillis()}")
            } catch (e: Exception) {
                Log.e(Constant.TAG, "Error saving bitmap to file:", e)
                null
            }
        }
    }


   private suspend fun viewToBitmap(view: View): Bitmap {
        val width = view.measuredWidth
        val height = view.measuredHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return bitmap
    }
}