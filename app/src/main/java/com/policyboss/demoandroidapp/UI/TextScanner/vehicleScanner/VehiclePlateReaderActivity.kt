package com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner


import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.policyboss.demoandroidapp.R


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class VehiclePlateReaderActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "VehiclePlateReader"
    }

    // UI components
    private lateinit var cameraPreview: PreviewView
    private lateinit var plateTextView: TextView
    private lateinit var btnDetect: Button
    private lateinit var btnBack: Button

    // Camera related
    private var camera: Camera? = null
    private lateinit var cameraExecutor: ExecutorService
    private val detectionRectangle = Rect(400, 300, 880, 500) // Fixed coordinates optimized for license plates

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_vehicle_plate_reader)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        // Initialize UI components
        cameraPreview = findViewById(R.id.cameraPreview)
        plateTextView = findViewById(R.id.plateTextView)
        btnDetect = findViewById(R.id.btnDetect)
        btnBack = findViewById(R.id.btnBack)

        // Initialize executor for camera operations
        cameraExecutor = Executors.newSingleThreadExecutor()



        btnDetect.setOnClickListener {
            val plateText = plateTextView.text.toString()
            if (plateText.isNotEmpty() && plateText != getString(R.string.plate_scan_align) &&
                plateText != getString(R.string.plate_scan_no_text)) {
                //returnResult(plateText)
                showToast(plateText)
            } else {
                showToast(getString(R.string.plate_scan_no_valid_plate))
            }
        }

        btnBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        // Check and request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions.launch(arrayOf(Manifest.permission.CAMERA))
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Set up the preview use case
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(cameraPreview.surfaceProvider)
            }

            // Configure image analysis for optimal vehicle plate detection
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageForPlateDetection(imageProxy)
                    }
                }

            // Always use the back camera for plate scanning
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind all use cases before rebinding
                cameraProvider.unbindAll()

                // Bind camera to the lifecycle
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )

                // Configure camera for optimal plate reading
                setupCameraForPlateReading()
            } catch (e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
                showToast("Camera initialization failed: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * Sets up camera parameters optimized for license plate reading
     */
    private fun setupCameraForPlateReading() {
        camera?.cameraControl?.apply {
            // Disable flash
            enableTorch(false)

            // Reset zoom to capture the full plate
            setLinearZoom(0.0f)

            // Configure auto-focus at the center where the plate is likely to be
            val meteringPoint = SurfaceOrientedMeteringPointFactory(1.0f, 1.0f)
                .createPoint(0.5f, 0.5f)

            val focusAction = FocusMeteringAction.Builder(meteringPoint)
                .setAutoCancelDuration(2, TimeUnit.SECONDS)
                .build()

            startFocusAndMetering(focusAction)
        }
    }

    @androidx.camera.core.ExperimentalGetImage
    private fun processImageForPlateDetection(imageProxy: androidx.camera.core.ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Initialize ML Kit text recognizer
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Get well-formatted text from within the detection rectangle
                    processDetectedText(visionText)
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Text recognition failed", e)
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    /**
     * Processes detected text to find and format vehicle plate numbers
     */
    private fun processDetectedText(visionText: Text) {
        // Extract text focused on the detection rectangle
        val detectedText = getLicensePlateText(visionText)

        if (detectedText.isNotEmpty()) {
            // Look for a valid vehicle number in the detected text
            val vehicleNumber = extractVehicleNumber(detectedText)

            runOnUiThread {
                if (vehicleNumber != null && isValidVehicleNumber(vehicleNumber)) {
                    // A valid plate number was found - format and display it
                    val formattedPlate = formatVehicleNumber(vehicleNumber)

                    plateTextView.text = formattedPlate
                    plateTextView.visibility = View.VISIBLE
                    plateTextView.setBackgroundResource(R.drawable.plate_background_success)
                    btnDetect.isEnabled = true

                    // Special case check for "UMESH"
                    if (detectedText.lowercase().contains("umesh")) {
                        showToast("Detected special plate: UMESH")
                    }
                } else {
                    // Text detected but not a valid plate number
                    // Show only the first line, limited to 20 chars
                    val firstLine = detectedText.split('\n').firstOrNull() ?: ""
                    val displayText = firstLine.take(20)

                    plateTextView.text = displayText
                    plateTextView.visibility = View.VISIBLE
                    plateTextView.setBackgroundResource(R.drawable.plate_background_neutral)
                    btnDetect.isEnabled = false
                }
            }
        } else {
            // No text was detected
            runOnUiThread {
                plateTextView.text = getString(R.string.plate_scan_no_text)
                plateTextView.visibility = View.VISIBLE
                plateTextView.setBackgroundResource(R.drawable.plate_background_error)
                btnDetect.isEnabled = false
            }
        }
    }

    /**
     * Gets license plate text from the detection rectangle
     * Focus on up to 2 lines max - optimal for standard Indian plates
     */
    private fun getLicensePlateText(visionText: Text): String {
        val plateTextLines = mutableListOf<String>()

        // Process blocks of text
        for (block in visionText.textBlocks) {
            // Process each line in the block
            for (line in block.lines) {
                val boundingBox = line.boundingBox ?: continue

                // Check if this line is within or intersects our detection rectangle
                // We consider a line relevant if:
                // 1. It's fully contained in our detection rectangle, OR
                // 2. It significantly overlaps with our detection rectangle
                if (isTextInDetectionZone(boundingBox, detectionRectangle)) {
                    plateTextLines.add(line.text)

                    // For license plates, we need at most 2 lines
                    // This handles both single-line plates and two-line plates
                    if (plateTextLines.size >= 2) {
                        break
                    }
                }
            }

            if (plateTextLines.size >= 2) {
                break
            }
        }

        // Join the lines with a newline character
        return plateTextLines.joinToString("\n")
    }

    /**
     * Determines if text is in the detection zone based on overlap percentage
     */
    private fun isTextInDetectionZone(textBox: Rect, detectionZone: Rect): Boolean {
        // Check if the text is completely inside the detection zone
        if (detectionZone.contains(textBox)) {
            return true
        }

        // Check for significant overlap (>50% of the text box area)
        val intersection = Rect()
        if (intersection.setIntersect(textBox, detectionZone)) {
            val intersectionArea = intersection.width() * intersection.height()
            val textBoxArea = textBox.width() * textBox.height()

            return intersectionArea > (textBoxArea * 0.5)
        }

        return false
    }

    /**
     * Extracts vehicle number from detected text using regex pattern
     */
    private fun extractVehicleNumber(text: String): String? {
        // Indian vehicle plate patterns - handles various formats
        val vehicleNumberPattern = Regex("[A-Z]{2}\\s*\\d{1,2}\\s*[A-Z]{1,2}\\s*\\d{1,4}")
        return vehicleNumberPattern.find(text)?.value
    }

    /**
     * Validates if the extracted text is a proper vehicle number
     */
    private fun isValidVehicleNumber(text: String): Boolean {
        val cleanText = text.replace("\\s".toRegex(), "")

        // Valid Indian vehicle numbers are 10-13 characters after removing spaces
        return cleanText.length in 10..13
    }

    /**
     * Formats vehicle number with proper spacing
     * Example: "MH01AB1234" -> "MH 01 AB 1234"
     */
    private fun formatVehicleNumber(vehicleNumber: String): String {
        try {
            // Remove any existing spaces
            val clean = vehicleNumber.replace("\\s".toRegex(), "")

            if (clean.length >= 10) {
                // Standard format: XX 00 XX 0000
                val stateCode = clean.substring(0, 2)
                val regionCode = clean.substring(2, 4)

                // Series can be 1 or 2 characters
                val seriesEndIndex = if (clean.length - 4 >= 2 + 4) 6 else 5
                val series = clean.substring(4, seriesEndIndex)

                // Last 4 digits are the number
                val number = clean.substring(clean.length - 4)

                return "$stateCode $regionCode $series $number"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting vehicle number", e)
        }

        // If formatting fails, return as-is
        return vehicleNumber
    }

    /**
     * Returns the detected plate text to the calling activity
     */
    private fun returnResult(plateText: String) {
        val resultIntent = Intent()
        resultIntent.putExtra("DETECTED_TEXT", plateText)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true) {
                startCamera()
            } else {
                showPermissionDeniedDialog()
            }
        }

    /**
     * Shows a dialog explaining why camera permission is needed
     */
    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Camera Permission Required")
            .setMessage("This app needs camera access to scan vehicle plates. Please grant permission.")
            .setPositiveButton("OK") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}