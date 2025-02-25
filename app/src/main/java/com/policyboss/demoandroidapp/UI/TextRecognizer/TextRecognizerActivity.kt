package com.policyboss.demoandroidapp.UI.TextRecognizer


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.policyboss.demoandroidapp.databinding.ActivityTextRecognizerBinding
import java.io.IOException

import android.content.Intent

import android.graphics.Rect
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.Executors



class TextRecognizerActivity : AppCompatActivity() {

    lateinit var binding : ActivityTextRecognizerBinding
    var CAMERA_PERMISSION_REQUEST_CODE = 101
    val TAG = "TextRecognizer"
    private lateinit var surfaceView: SurfaceView
    private lateinit var overlayView: View
    private lateinit var cameraSource: CameraSource
    private lateinit var previewSize: Size

    private val rectPaint = Paint()
    private var rect: Rect? = null

    // var detectionArea = Rect(100, 100, 300, 300) // x,y,width,height


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityTextRecognizerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val textRecognizer = TextRecognizer.Builder(this).build()
        // Initialize the SurfaceView and overlayView
        surfaceView = binding.surfaceView

        // Initialize the CameraSource

        binding.btnClose.setOnClickListener{

            this.finish()
        }



        // Initialize the CameraSource
        cameraSource = CameraSource.Builder(this,textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
           // .setRequestedPreviewSize(1280, 720)
           // .setRequestedFps(30.0f)
            .setAutoFocusEnabled(true)
            .build()


        // Add a callback to the surfaceView's holder
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@TextRecognizerActivity,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        return
                    }
                    cameraSource.start(holder)



                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // No need to handle this callback
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        // Perform text detection on the camera preview
        performTextDetection()
    }



    private fun performTextDetection() {

    }

    override fun onDestroy() {
        super.onDestroy()

        if (cameraSource != null) {
            cameraSource.release();
        }
//        if (textRecognizer != null) {
//            textRecognizer.release();
//        }
    }

    fun checkPermission(){


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            //startCamera()
        }

    }


    fun textRecog(){

//        textRecognizer.setProcessor(object : Detector.Processor<TextBlock?> {
//            override fun release() {}
//            override fun receiveDetections(detections: Detections<TextBlock?>) {
//                // graphicOverlay.clear()
//                val items = detections.detectedItems
//                for (i in 0 until items.size()) {
//                    val textBlock = items.valueAt(i)
//                    if (textBlock != null && textBlock.value != null) {
//                        val rect = RectF(textBlock.boundingBox)
//                        //  graphicOverlay.add(OcrGraphic(graphicOverlay, rect, textBlock.value))
//                        if (detectionArea.contains(
//                                rect.left.toInt(),
//                                rect.top.toInt(),
//                                rect.right.toInt(),
//                                rect.bottom.toInt()
//                            )
//                        ) {
//                            Log.d(TAG, "Recognized text: " + textBlock.value)
//                            // update UI with recognized text
//                            runOnUiThread {}
//                        }
//                    }
//                }
//            }
//        })
    }



    fun dataText(){

//        val items: SparseArray<TextBlock> = detections.detectedItems
//        if (items.size() != 0) {
//            val stringBuilder = StringBuilder()
//            for (i in 0 until items.size()) {
//                val item: TextBlock = items.valueAt(i)
//                stringBuilder.append(item.value)
//                stringBuilder.append("\n")
//            }
//
//
        }


  }


