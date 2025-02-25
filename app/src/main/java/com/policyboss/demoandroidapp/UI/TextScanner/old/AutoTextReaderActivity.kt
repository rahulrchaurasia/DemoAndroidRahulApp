package com.policyboss.demoandroidapp.UI.TextScanner.old

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import kotlin.math.max
import com.policyboss.demoandroidapp.databinding.ActivityAutoTextReaderBinding
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executors
import kotlin.math.min


class AutoTextReaderActivity : BaseActivity() {


    private lateinit var binding: ActivityAutoTextReaderBinding

    private val cameraExecutor = Executors.newSingleThreadExecutor()
    //private lateinit var textRecognizer: TextRecognition

    var inputImage : InputImage? = null
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


                    // Calculate the crop region based on the overlayView's position and dimensions
                    val overlayRect = Rect(
                        binding.overlayView.left,
                        binding.overlayView.top,
                        binding.overlayView.right,
                        binding.overlayView.bottom
                    )
                    val scaleFactorX = image.width.toFloat() / binding.cameraView.width
                    val scaleFactorY = image.height.toFloat() / binding.cameraView.height

                    val left = (overlayRect.left * scaleFactorX).toInt()
                    val top = (overlayRect.top * scaleFactorY).toInt()
                    val right = (overlayRect.right * scaleFactorX).toInt()
                    val bottom = (overlayRect.bottom * scaleFactorY).toInt()



                    // Ensure the cropRect is within the image bounds
                    val cropRect = Rect(
                        max(left, 0),
                        max(top, 0),
                        min(right, image.width),
                        min(bottom, image.height)
                    )



                   // Convert the ImageProxy to a Bitmap using the imageProxyToBitmap function
                    val sourceBitmap = imageProxyToBitmap(imageProxy)

                    if (sourceBitmap != null) {
                        // Create a cropped image
                        val croppedImage = Bitmap.createBitmap(
                            sourceBitmap!!,
                            cropRect.left,
                            cropRect.top,
                            cropRect.width(),
                            cropRect.height()
                        )

                        // Determine the rotation of the original image
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

                        // Apply the rotation to the cropped image
                        val rotatedCroppedImage = when (rotationDegrees) {
                            90 -> croppedImage.rotate(90f)
                            180 -> croppedImage.rotate(180f)
                            270 -> croppedImage.rotate(270f)
                            else -> croppedImage
                        }
                        // Create an InputImage object from the cropped image
                        val inputImage = InputImage.fromBitmap(
                            croppedImage,
                            imageProxy.imageInfo.rotationDegrees
                        )



                        Log.d(Constant.TAG, "Input Image" + inputImage)


                        textRecognizer.process(inputImage)
                            .addOnSuccessListener { visionText ->
                                // Handle recognized text here
                                val detectedText = visionText.text
                                binding.textResult.text = detectedText
                            }
                            .addOnFailureListener { e ->
                                // Handle any errors here
                                binding.textResult.text = "No Data "
                            }
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


    fun captureView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    fun imageProxyToBitmap2(imageProxy: ImageProxy): Bitmap? {
        val plane = imageProxy.planes[0]
        val buffer = plane.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val bufferImage = YuvImage(
            bytes, ImageFormat.NV21, imageProxy.width, imageProxy.height, null
        )

        val outputStream = ByteArrayOutputStream()
        bufferImage.compressToJpeg(Rect(0, 0, imageProxy.width, imageProxy.height), 100, outputStream)
        val jpegData = outputStream.toByteArray()

        return BitmapFactory.decodeByteArray(jpegData, 0, jpegData.size)
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
        return imageProxy?.toBitmap()
    }

    // Extension function to rotate a Bitmap
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }


    fun aboveCode(){

//        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), { imageProxy ->
//            val image = imageProxy.image
//            if (image != null) {
//
//
//                // Calculate the crop region based on the overlayView's position and dimensions
//                val overlayRect = Rect(
//                    binding.overlayView.left,
//                    binding.overlayView.top,
//                    binding.overlayView.right,
//                    binding.overlayView.bottom
//                )
//                val scaleFactorX = image.width.toFloat() / binding.cameraView.width
//                val scaleFactorY = image.height.toFloat() / binding.cameraView.height
//
//                val left = (overlayRect.left * scaleFactorX).toInt()
//                val top = (overlayRect.top * scaleFactorY).toInt()
//                val right = (overlayRect.right * scaleFactorX).toInt()
//                val bottom = (overlayRect.bottom * scaleFactorY).toInt()
//
////                    val cropRect = Rect(
////                        (overlayRect.left * scaleFactorX).toInt(),
////                        (overlayRect.top * scaleFactorY).toInt(),
////                        (overlayRect.right * scaleFactorX).toInt(),
////                        (overlayRect.bottom * scaleFactorY).toInt()
////                    )
//
//                // Ensure the cropRect is within the image bounds
//                val cropRect = Rect(
//                    max(left, 0),
//                    max(top, 0),
//                    min(right, image.width),
//                    min(bottom, image.height)
//                )
//
////                    val croppedImage = Bitmap.createBitmap(
////
////                        cropRect.width(),
////                        cropRect.height(),
////                        Bitmap.Config.ARGB_8888
////                    )
//
//
//                // Convert the ImageProxy to a Bitmap using the imageProxyToBitmap function
//                val sourceBitmap = imageProxyToBitmap(imageProxy)
//
//                if (sourceBitmap != null) {
//                    // Create a cropped image
//                    val croppedImage = Bitmap.createBitmap(
//                        sourceBitmap!!,
//                        cropRect.left,
//                        cropRect.top,
//                        cropRect.width(),
//                        cropRect.height()
//                    )
//
//                    // Determine the rotation of the original image
//                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
//
//                    // Apply the rotation to the cropped image
//                    val rotatedCroppedImage = when (rotationDegrees) {
//                        90 -> croppedImage.rotate(90f)
//                        180 -> croppedImage.rotate(180f)
//                        270 -> croppedImage.rotate(270f)
//                        else -> croppedImage
//                    }
//                    // Create an InputImage object from the cropped image
//                    val inputImage = InputImage.fromBitmap(
//                        croppedImage,
//                        imageProxy.imageInfo.rotationDegrees
//                    )
//
//                    Log.d(Constant.TAG, "Input Image" + inputImage)
//
//                    // Get the Bitmap object from the ImageView object.
////                    val bitmap =  binding.imgLogo.drawable.toBitmap()
////
////                     // Create an InputImage object from the Bitmap object.
////                    val inputImage = InputImage.fromBitmap(bitmap,imageProxy.imageInfo.rotationDegrees)
//
//                    textRecognizer.process(inputImage)
//                        .addOnSuccessListener { visionText ->
//                            // Handle recognized text here
//                            val detectedText = visionText.text
//                            binding.textResult.text = detectedText
//                        }
//                        .addOnFailureListener { e ->
//                            // Handle any errors here
//                            binding.textResult.text = "No Data "
//                        }
//                }
//
//
//            }
//            imageProxy.close()
//        })

    }
}
