package com.policyboss.demoandroidapp.UI.TextScanner

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageUtils
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.policyboss.demoandroidapp.BaseActivity

import com.policyboss.demoandroidapp.databinding.ActivityAutoTextReaderBinding
import java.util.concurrent.Executors


class AutoTextReaderActivity : BaseActivity() {


    private lateinit var binding: ActivityAutoTextReaderBinding

    private val cameraExecutor = Executors.newSingleThreadExecutor()
    //private lateinit var textRecognizer: TextRecognition


    @androidx.annotation.OptIn(ExperimentalGetImage::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAutoTextReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textResult.text = " Started Here..."
       // textRecognizer = TextRecognition.getClient()
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
           // val ori = binding.overlayView.convertImageProxyToBitmap().rotate(90f)
            val cameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.cameraView.surfaceProvider) }



            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), { imageProxy ->
                val image = imageProxy.image
                if (image != null) {

                    // Crop the image to the overlayView's area
                    val overlayRect = Rect(binding.overlayView.left, binding.overlayView.top, binding.overlayView.right, binding.overlayView.bottom)
                    val scaleFactorX = image.width.toFloat() / binding.cameraView.width
                    val scaleFactorY = image.height.toFloat() / binding.cameraView.height
                    val cropRect = Rect(
                        (overlayRect.left * scaleFactorX).toInt(),
                        (overlayRect.top * scaleFactorY).toInt(),
                        (overlayRect.right * scaleFactorX).toInt(),
                        (overlayRect.bottom * scaleFactorY).toInt()
                    )


                    val croppedImage = Bitmap.createBitmap(

                        cropRect.width(),
                        cropRect.height(),
                        Bitmap.Config.ARGB_8888
                    )
                    // Create an InputImage object from the cropped image
                    val inputImage = InputImage.fromBitmap(croppedImage,imageProxy.imageInfo.rotationDegrees)

                    // Get the Bitmap object from the ImageView object.
//                    val bitmap =  binding.imgLogo.drawable.toBitmap()
//
//                     // Create an InputImage object from the Bitmap object.
//                    val inputImage = InputImage.fromBitmap(bitmap,imageProxy.imageInfo.rotationDegrees)

                    textRecognizer.process(inputImage)
                        .addOnSuccessListener { visionText ->
                            // Handle recognized text here
                            val detectedText = visionText.text
                            binding.textResult.text = detectedText
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors here
                            binding.textResult.text = e.message.toString()
                        }


                }
                imageProxy.close()
            })

           // cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, binding.cameraView)
            cameraProvider.bindToLifecycle(this@AutoTextReaderActivity,cameraSelector,imageAnalysis,preview)
           // cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


}
