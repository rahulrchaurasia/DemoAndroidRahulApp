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
import android.view.ViewTreeObserver
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
    private lateinit var plateDetectionZone : View

    private lateinit var detectionOverlay: DetectionOverlayView //005


    // Camera related
    private var camera: Camera? = null
    private lateinit var cameraExecutor: ExecutorService
   // private val detectionRectangle = Rect(400, 300, 880, 500) // Fixed coordinates optimized for license plates

    // Define the detection rectangle that will be initialized once layout is complete
    private var detectionRectangle = Rect()

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
        plateDetectionZone = findViewById(R.id.plateDetectionZone)

        detectionOverlay = findViewById(R.id.detectionOverlay)


        // Setup the detection rectangle once the layout is ready
       // setupDetectionRectangle()
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



//    private fun setupDetectionRectangle() {
//
//        plateDetectionZone.post {
//            // Get view position on screen
//            val locationOnScreen = IntArray(2)
//            plateDetectionZone.getLocationOnScreen(locationOnScreen)
//
//            // Set detection rectangle to match the view's position
//            // No need for dpToPx conversion here - width and height are already in pixels
//            detectionRectangle.set(
//                locationOnScreen[0],
//                locationOnScreen[1],
//                locationOnScreen[0] + plateDetectionZone.width,
//                locationOnScreen[1] + plateDetectionZone.height
//            )
//
//            Log.d(TAG, "Detection rectangle set to: $detectionRectangle")
//        }
//    }
//
//    private fun dpToPx(dp: Int): Int {
//        return (dp * resources.displayMetrics.density).toInt()
//    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Set up the preview use case
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = cameraPreview.surfaceProvider
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

            // Get detection rectangle from the overlay view in image coordinates
            val imageWidth = imageProxy.width
            val imageHeight = imageProxy.height
            detectionRectangle = detectionOverlay.getDetectionRectInImageCoordinates(imageWidth, imageHeight)

            Log.d(TAG, "Detection rectangle: $detectionRectangle")

            // Continue with text recognition...
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
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

                // Special case check for "UMESH"
                if (detectedText.lowercase().contains("umesh")) {
                    showToast("Detected special plate: UMESH")
                }

                if (vehicleNumber != null && isValidVehicleNumber(vehicleNumber)) {
                    // A valid plate number was found - format and display it
                    val formattedPlate = formatVehicleNumber(vehicleNumber)

                    plateTextView.text = formattedPlate
                    plateTextView.visibility = View.VISIBLE
                    plateTextView.setBackgroundResource(R.drawable.plate_background_success)
                    btnDetect.isEnabled = true

                    returnResult(formattedPlate)
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
     * Calculate a score based on overlap and position within the detection zone
     * Higher score = better match for license plate
     */

    //005 temp added
    private fun calculateOverlapScore(textBox: Rect, detectionZone: Rect): Float {
        val intersection = Rect()
        if (!intersection.setIntersect(textBox, detectionZone)) {
            return 0f
        }

        // Calculate overlap percentage
        val intersectionArea = intersection.width() * intersection.height()
        val textBoxArea = textBox.width() * textBox.height()
        val overlapPercentage = intersectionArea.toFloat() / textBoxArea.toFloat()

        // Calculate distance from center (normalized to 0-1, where 0 is center)
        val textCenterX = textBox.centerX()
        val textCenterY = textBox.centerY()
        val zoneCenterX = detectionZone.centerX()
        val zoneCenterY = detectionZone.centerY()

        val maxPossibleDistance = Math.sqrt(
            Math.pow(detectionZone.width() / 2.0, 2.0) +
                    Math.pow(detectionZone.height() / 2.0, 2.0)
        ).toFloat()

        val actualDistance = Math.sqrt(
            Math.pow((textCenterX - zoneCenterX).toDouble(), 2.0) +
                    Math.pow((textCenterY - zoneCenterY).toDouble(), 2.0)
        ).toFloat()

        val normalizedDistance = actualDistance / maxPossibleDistance
        val proximityScore = 1f - normalizedDistance

        // Weight proximity higher than pure overlap
        return (overlapPercentage * 0.4f) + (proximityScore * 0.6f)
    }
    /**
     * Gets license plate text from the detection rectangle
     * Focus on up to 2 lines max - optimal for standard Indian plates
     */
    private fun getLicensePlateTextOLD(visionText: Text): String {
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
     * Gets license plate text from the detection rectangle
     * Focus on up to 2 lines max - optimal for standard Indian plates
     */
    private fun getLicensePlateText(visionText: Text): String {
        val plateTextLines = mutableListOf<String>()
        val candidateLines = mutableListOf<Pair<String, Float>>()

        // Process blocks of text
        for (block in visionText.textBlocks) {
            // Process each line in the block
            for (line in block.lines) {
                val boundingBox = line.boundingBox ?: continue

                // Check if this line is within or intersects our detection rectangle
                if (isTextInDetectionZone(boundingBox, detectionRectangle)) {
                    // Calculate overlap percentage to prioritize text
                    val overlapScore = calculateOverlapScore(boundingBox, detectionRectangle)

                    // Add to candidates with overlap score
                    candidateLines.add(Pair(line.text, overlapScore))
                }
            }
        }

        // Sort by overlap score (higher score first)
        candidateLines.sortByDescending { it.second }

        // Take the top 2 lines maximum
        val maxLines = minOf(2, candidateLines.size)
        for (i in 0 until maxLines) {
            plateTextLines.add(candidateLines[i].first)
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

            return intersectionArea > (textBoxArea * 0.3)
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