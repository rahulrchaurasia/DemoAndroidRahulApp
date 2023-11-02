package com.policyboss.demoandroidapp.UI.CropImage

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.databinding.ActivityCameraTextDetectBinding


import java.util.concurrent.ExecutorService


class CameraCropImageActivity : AppCompatActivity() {

    lateinit var binding : ActivityCameraTextDetectBinding

    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private lateinit var cameraExecutor: ExecutorService
    private val TAG = "TextScanner"



   


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraTextDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.imgClose.setOnClickListener{

            this@CameraCropImageActivity.finish()
        }

        binding.btnSubmit.setOnClickListener{


            val data = captureView(binding.overlayView)

            Log.d(Constant.TAG , ""+ data.toString().length)
        }

//// Get the drawing cache of the view
//val drawingCache = view.getDrawingCache(
    }


    // Use this...
//// val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

    fun captureView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap

    }




}