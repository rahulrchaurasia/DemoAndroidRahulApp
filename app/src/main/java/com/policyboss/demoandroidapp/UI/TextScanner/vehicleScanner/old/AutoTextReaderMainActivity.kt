package com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.old

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import com.policyboss.demoandroidapp.R

import java.util.concurrent.Executors

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect

import android.util.Size
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn

import androidx.camera.core.Camera
import androidx.camera.core.ExperimentalGetImage

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.ActivityAutoTextReaderMainBinding


class AutoTextReaderMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAutoTextReaderMainBinding
    private lateinit var cameraPreview: PreviewView
    private lateinit var ovalTextView: TextView
    private var camera: Camera? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAutoTextReaderMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraPreview = findViewById(R.id.cameraPreview)
        ovalTextView = findViewById(R.id.ovalTextView)

        // Check and request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions.launch(arrayOf(Manifest.permission.CAMERA))
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Set up the preview use case
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = cameraPreview.surfaceProvider
            }

            // Set up the image analysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(Executors.newSingleThreadExecutor(), { imageProxy ->
                        processImage(imageProxy)
                    })
                }

            // Select the back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind all use cases before rebinding
                cameraProvider.unbindAll()

                // Bind the camera to the lifecycle
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    @OptIn(ExperimentalGetImage::class)
    private fun processImage(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Initialize the text recognizer
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->

                    // Extract all detected text
                   val detectedText = getFirstLineTextInRectangle1(visionText)

                    if (detectedText.isNotEmpty()) {
                        // Show the detected text in the white oval rectangle
                        ovalTextView.text = detectedText
                        ovalTextView.visibility = TextView.VISIBLE

                        val vehicleNumber = extractVehicleNumber(detectedText)

                        Log.d(Constant.TAG, "detectedText =" + detectedText.toString())

                        if (detectedText.lowercase().contains("umesh".lowercase())) {
                            showToast("Valid vehicle number: $detectedText")
                        }


                        // Check if the detected text contains a valid vehicle number

                        if  (vehicleNumber != null && isValidVehicleNumber(vehicleNumber)) {
                            // If valid vehicle number, proceed with the result
                           // returnResult(vehicleNumber)
                            showToast("Valid vehicle number: $detectedText")
                        } else {
                            // Optionally, show a message that no valid vehicle number was found
                          //  Log.d(Constant.TAG, "No valid vehicle number found in detected text.")

                        }

                    } else {
                        // Hide the ovalTextView if no text is detected in the rectangle
                        ovalTextView.visibility = View.GONE
                    }

                    // Close the ImageProxy after processing is complete
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    Log.e("TextRecognition", "Text recognition failed", e)
                    // Close the ImageProxy even if processing fails
                    imageProxy.close()
                }
        } else {
            // Close the ImageProxy if mediaImage is null
            imageProxy.close()
        }
    }


    private fun getAllTextInRectangle(visionText: com.google.mlkit.vision.text.Text): String {
        // Define the rectangle area (adjust coordinates as needed)
        val rectangle = Rect(500, 500, 800, 600) // Example rectangle

        val textBuilder = StringBuilder()

        // Iterate through the text blocks, lines, and elements
        for (textBlock in visionText.textBlocks) {
            for (line in textBlock.lines) {
                for (element in line.elements) {
                    val boundingBox = element.boundingBox
                    if (boundingBox != null && rectangle.contains(boundingBox)) {
                        // Append the text of the line
                        textBuilder.append(line.text).append("\n")
                    }
                }
            }
        }

        // Return all detected text within the rectangle
        return textBuilder.toString().trim()
    }


    //region Not in used
    private fun getFirstLineTextInRectangleOLD2(visionText: com.google.mlkit.vision.text.Text): String {
        // Define the rectangle area (adjust coordinates as needed)
        val rectangle = Rect(500, 500, 800, 600) // Example rectangle

        // Iterate through the text blocks, lines, and elements
        for (textBlock in visionText.textBlocks) {
            for (line in textBlock.lines) {
                for (element in line.elements) {
                    val boundingBox = element.boundingBox
                    if (boundingBox != null && rectangle.contains(boundingBox)) {
                        // Return the first line of text within the rectangle
                        return line.text
                    }
                }
            }
        }

        // Return an empty string if no text is found within the rectangle
        return ""
    }


    private fun isTextInRectangleOLD(visionText: com.google.mlkit.vision.text.Text): Boolean {
        // Define the rectangle area (adjust coordinates as needed)
        val rectangle = Rect(500, 500, 800, 600) // Example rectangle

        // Check if any text block is within the rectangle
        for (textBlock in visionText.textBlocks) {
            for (line in textBlock.lines) {
                for (element in line.elements) {
                    val boundingBox = element.boundingBox
                    if (boundingBox != null && rectangle.contains(boundingBox)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    //get Only two line
    private fun getFirstLineTextInRectangle1(visionText: com.google.mlkit.vision.text.Text): String {
        // Define the rectangle area (adjust coordinates as needed)
        val rectangle = Rect(500, 500, 800, 600) // Example rectangle

        val textBuilder = StringBuilder()
        var lineCount = 0

        // Iterate through the text blocks, lines, and elements
        for (textBlock in visionText.textBlocks) {
            for (line in textBlock.lines) {
                for (element in line.elements) {
                    val boundingBox = element.boundingBox
                    if (boundingBox != null && rectangle.contains(boundingBox)) {
                        // Append the text of the line
                        textBuilder.append(line.text).append("\n")
                        lineCount++

                        // Stop after two lines
                        if (lineCount >= 2) {
                            break
                        }
                    }
                }
                if (lineCount >= 2) {
                    break
                }
            }
            if (lineCount >= 2) {
                break
            }
        }

        // Trim the result and limit to 25 characters
        return textBuilder.toString().trim().take(25)
    }

    //get Only two line and merge it
    private fun getFirstLineTextInRectangle(visionText: com.google.mlkit.vision.text.Text): String {
        val rectangle = Rect(500, 500, 800, 600) // Adjust as needed
        val detectedLines = mutableListOf<String>()

        for (textBlock in visionText.textBlocks) {
            for (line in textBlock.lines) {
                val boundingBox = line.boundingBox
                if (boundingBox != null && rectangle.contains(boundingBox)) {
                    detectedLines.add(line.text.trim()) // Add detected line

                    // Stop after collecting two lines
                    if (detectedLines.size >= 2) {
                        break
                    }
                }
            }
            if (detectedLines.size >= 2) {
                break
            }
        }

        // Join the two lines into a single string and limit to 25 characters
        return detectedLines.joinToString(" ").take(25)
    }

    //endregion

    private fun returnResult(detectedText: String) {
        val resultIntent = Intent()
        Log.d(Constant.TAG, "Detected Text is $detectedText" )
        resultIntent.putExtra("DETECTED_TEXT", detectedText)
        setResult(RESULT_OK, resultIntent)
        finish() // Close the activity
    }

    private fun allPermissionsGranted() = arrayOf(Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true) {
                startCamera()
            } else {
                Log.e("Permissions", "Camera permission denied")
            }
        }


    private fun isVehicleNumber(text: String): Boolean {
        val cleanText = text.replace("[\\s-]".toRegex(), "")

        // Vehicle number patterns
        return cleanText.matches(Regex("^[A-Z]{2}\\d{2}[A-Z]{1,2}\\d{4}[A-Z]?$")) || // New format
                cleanText.matches(Regex("^[A-Z]{2}\\d{4}$")) // Old format
    }


    // CHANGE: Improved vehicle number validation logic
    private fun isValidVehicleNumber(text: String): Boolean {
        // Regex to match vehicle numbers like "MH 01 AB 1234" or "MH01AB1234"
        val vehicleNumberPattern = Regex("""[A-Z]{2}\s?\d{1,2}\s?[A-Z]{1,2}\s?\d{1,4}""")
        val cleanText = text.replace("\\s".toRegex(), "") // Remove spaces
        return vehicleNumberPattern.matches(cleanText) && cleanText.length in 10..13 // Validate length
    }

    // CHANGE: Extract vehicle number from detected text
    private fun extractVehicleNumber(text: String): String? {
        val vehicleNumberPattern = Regex("""[A-Z]{2}\s?\d{1,2}\s?[A-Z]{1,2}\s?\d{1,4}""")
        return vehicleNumberPattern.find(text)?.value?.replace("\\s".toRegex(), "")
    }
}