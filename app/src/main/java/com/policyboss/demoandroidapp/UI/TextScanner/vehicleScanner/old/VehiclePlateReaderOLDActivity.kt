package com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.old


import com.policyboss.demoandroidapp.R


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.DetectionOverlayView
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/*
Note : "Executors.newSingleThreadExecutor()" creates an executor service with a single background thread for executing tasks sequentially.

How It Works
It queues tasks and runs them one at a time on a single worker thread.
 */

class VehiclePlateReaderOLDActivity : AppCompatActivity() {

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

        setContentView(R.layout.activity_vehicle_plate_reader_old)


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

            runOnUiThread {
                val plateText = plateTextView.text.toString()
                if (plateText.isNotEmpty() && plateText != getString(R.string.plate_scan_no_text)) {
                    returnResult(plateText)
                    // showToast(plateText)
                }
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

        runOnUiThread {
            // Extract text focused on the detection rectangle
            val detectedText = getLicensePlateText(visionText)

            // Then wait a moment to let the UI update before finishing


            if (detectedText.isNotEmpty()) {


                    // Look for a valid vehicle number in the detected text

                    Log.d(Constant.TAG, "detectedText getLicensePlateText: $detectedText")

                    // Extract and clean the vehicle number (remove spaces)
                    val vehicleNumber = extractVehicleNumber(detectedText)

                    if (!vehicleNumber.isNullOrEmpty()) {
                        Log.d(
                            Constant.TAG,
                            "vehicleNumber after extractVehicleNumber : $vehicleNumber"
                        )

                        // region comment Special case check for "UMESH" Text

//                    if (detectedText.lowercase().contains("umesh")) {
//                        showToast("Detected special plate: UMESH")
//                    }
                        //endregion

                        if (isValidVehicleNumber(vehicleNumber)) {
                            // A valid plate number was found - format and display it


                            Log.d(Constant.TAG, "valid vehicleNumber : $vehicleNumber")
                            val formattedPlate = formatVehicleNumber(vehicleNumber)


                            Log.d(Constant.TAG, "format VehicleNumber  : $formattedPlate")

                            // First update the UI
                            plateTextView.text = formattedPlate

                            plateTextView.visibility = View.VISIBLE
                            plateTextView.setBackgroundResource(R.drawable.plate_background_success)
                            // btnDetect.isEnabled = true

                            // Then use Handler to delay the activity finish
                            Handler(Looper.getMainLooper()).postDelayed({
                                returnResult(formattedPlate)
                            }, 400) // 500ms delay


                            //returnResult(formattedPlate)
                        }

                        else {

                            // Vehicle number found but not valid
                            Log.d(Constant.TAG, "Invalid vehicle number format: $vehicleNumber")
                            plateTextView.text = vehicleNumber
                            plateTextView.visibility = View.VISIBLE
                            plateTextView.setBackgroundResource(R.drawable.plate_background_neutral)

                        }
                    }
                    else {
                        // *** CRITICAL CHANGE: Added handler for null vehicleNumber ***
                        // No vehicle number pattern found, but we have text
                        val firstLine = detectedText.split('\n').firstOrNull() ?: ""
                        val displayText = firstLine.take(20)

                        Log.d(Constant.TAG, "No valid vehicle number pattern detected in: $displayText")
                        plateTextView.text = displayText
                        plateTextView.visibility = View.VISIBLE
                        plateTextView.setBackgroundResource(R.drawable.plate_background_neutral)
                    }


            }
            else {
                // No text was detected
                    plateTextView.text = getString(R.string.plate_scan_no_text)
                    plateTextView.visibility = View.VISIBLE
                    plateTextView.setBackgroundResource(R.drawable.plate_background_error)
                    // btnDetect.isEnabled = false

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
     * Returns the detected plate text to the calling activity
     */
    private fun returnResult(plateText: String) {
        val resultIntent = Intent()
        Log.d(Constant.TAG, "Detected Text is $plateText")

        //  resultIntent.putExtra(Constant.KEY_DETECT_TEXT, plateText)
        resultIntent.putExtra(Constant.KEY_DETECT_TEXT, plateText.toByteArray(Charsets.UTF_8))
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



    /**
     * Extracts vehicle number from detected text using improved regex pattern
     * with validation for valid Indian state codes
     */

    /**
     * Extracts vehicle number from detected text
     * Handles spaces, dots and other special characters
     */
    private fun extractVehicleNumber(text: String): String? {
        Log.d(Constant.TAG, "Input text for extraction: $text")

        // List of valid Indian state codes for validation
        val validStateCodes = setOf(
            "AP", "AR", "AS", "BR", "CG", "CH", "DD", "DL", "DN", "GA",
            "GJ", "HP", "HR", "JH", "JK", "KA", "KL", "LA", "LD", "MH",
            "ML", "MN", "MP", "MZ", "NL", "OD", "PB", "PY", "RJ", "SK",
            "TN", "TR", "TS", "UK", "UP", "WB"
        )

        // CHANGE 1: Clean the text by removing all non-alphanumeric characters
        // This handles spaces, dots, and other special characters
        // Changed from: val cleanText = text.replace("\\s".toRegex(), "")
        val cleanText = text.replace("[^A-Za-z0-9]".toRegex(), "").uppercase()
        Log.d(Constant.TAG, "Cleaned text (special chars removed): $cleanText")

        // CHANGE 2: Look for standard vehicle number patterns
        val vehicleNumberPattern = Regex("[A-Z]{2}\\d{1,2}[A-Z]{1,3}\\d{1,4}")
        val matches = vehicleNumberPattern.findAll(cleanText).toList()

        // First check for plates that start with valid state codes
        for (match in matches) {
            val potentialPlate = match.value
            if (potentialPlate.length >= 2) {
                val stateCode = potentialPlate.substring(0, 2)
                if (validStateCodes.contains(stateCode)) {
                    Log.d(Constant.TAG, "Found match with valid state code: $potentialPlate")
                    return potentialPlate
                }
            }
        }

        // CHANGE 3: CRITICAL FIX FOR YOUR "H01EN4382" ISSUE
        // This section adds the missing first letter when it detects plates like "H01EN..."
        if (cleanText.length >= 2) {
            val firstChar = cleanText[0]
            // Check if the first character is a valid second letter of a state code (H is 2nd letter in MH)
            for (stateCode in validStateCodes) {
                if (stateCode[1] == firstChar) {
                    // Try prepending the first letter of the state code (adding M before H)
                    val correctedText = stateCode[0] + cleanText
                    val correctedMatches = vehicleNumberPattern.findAll(correctedText).toList()
                    if (correctedMatches.isNotEmpty()) {
                        val potentialPlate = correctedMatches.first().value
                        Log.d(Constant.TAG, "Corrected plate by adding state prefix: $potentialPlate")
                        return potentialPlate
                    }
                }
            }
        }

        // CHANGE 4: Added fallback options for when the main pattern doesn't match

        // If no matches found with valid state codes, return the first match
        if (matches.isNotEmpty()) {
            Log.d(Constant.TAG, "No valid state code found, returning first match: ${matches.first().value}")
            return matches.first().value
        }

        // Try a more lenient pattern as last resort
        val simplifiedPattern = Regex("[A-Z0-9]{8,11}")
        val simpleMatches = simplifiedPattern.findAll(cleanText).toList()
        if (simpleMatches.isNotEmpty()) {
            val potentialPlate = simpleMatches.first().value
            Log.d(Constant.TAG, "Found potential plate with simplified pattern: $potentialPlate")
            return potentialPlate
        }

        Log.d(Constant.TAG, "No vehicle number pattern detected")
        return null
    }


    /**
     * Validates if the extracted text is a proper vehicle number
     */
    private fun isValidVehicleNumber(text: String): Boolean {
        // Clean the input before validation - remove all non-alphanumeric characters
        val cleanText = text.replace("[^A-Za-z0-9]".toRegex(), "").uppercase()
        Log.d(Constant.TAG, "Validating vehicle number (cleaned): $cleanText")

        // Indian vehicle numbers are typically 9-11 characters long
        if (cleanText.length !in 9..11) {
            Log.d(Constant.TAG, "Invalid length: ${cleanText.length} characters (must be 9-11)")
            return false
        }

        // List of valid Indian state codes for validation
        val validStateCodes = setOf(
            "AP", "AR", "AS", "BR", "CG", "CH", "DD", "DL", "DN", "GA",
            "GJ", "HP", "HR", "JH", "JK", "KA", "KL", "LA", "LD", "MH",
            "ML", "MN", "MP", "MZ", "NL", "OD", "PB", "PY", "RJ", "SK",
            "TN", "TR", "TS", "UK", "UP", "WB"
        )

        // Check if the license plate starts with a valid state code
        val stateCode = cleanText.substring(0, 2)
        if (!validStateCodes.contains(stateCode)) {
            Log.d(Constant.TAG, "Invalid state code: $stateCode")
            return false
        }

        // Updated regex to match the structure of Indian vehicle numbers
        // This allows for 1-2 digits after state code, followed by 1-3 letters, followed by 4 digits
        val regex = "^[A-Z]{2}\\d{1,2}[A-Z]{1,3}\\d{4}$".toRegex()

        val isValid = regex.matches(cleanText)

        // Debug logging to see if the regex is matching
        if (!isValid) {
            Log.d(Constant.TAG, "Regex match failed for: $cleanText")

            // Break down the components to see which part fails
            val remainingText = cleanText.substring(2)
            val digitsCount = remainingText.count { it.isDigit() }
            val lettersCount = remainingText.count { it.isLetter() }

            Log.d(Constant.TAG, "Structure breakdown - State: $stateCode, Remaining Digits: $digitsCount, Remaining Letters: $lettersCount")
        } else {
            Log.d(Constant.TAG, "Vehicle number validated successfully")
        }

        return isValid
    }

    /**
     * Formats vehicle number by removing all non-alphanumeric characters
     * Handles spaces, dots, and other special characters
     */
    private fun formatVehicleNumber(vehicleNumber: String): String {
        // Remove all non-alphanumeric characters and convert to uppercase
        return vehicleNumber.replace("[^A-Za-z0-9]".toRegex(), "").uppercase()
    }


    /**
     * Extracts vehicle number from detected text using regex pattern
     */

    private fun extractVehicleNumberOLD(text: String): String? {
        Log.d(Constant.TAG, "Input text for extraction: $text")

        // Remove all spaces from the input text
        val cleanText = text.replace("\\s".toRegex(), "")

        // Improved regex pattern to match Indian vehicle numbers
        val vehicleNumberPattern = Regex("\\b[A-Z]{2}\\d{1,2}[A-Z]{1,3}\\d{1,4}\\b")
        val matchedValue = vehicleNumberPattern.find(cleanText)?.value

        Log.d(Constant.TAG, "Matched vehicle number: $matchedValue")
        return matchedValue
    }


    /**
     * Validates if the extracted text is a proper vehicle number
     */

    private fun isValidVehicleNumberOLD(text: String): Boolean {
        // Indian vehicle numbers are typically 9-10 characters long
        if (text.length !in 9..10) {
            return false
        }

        // Regex to match the structure of Indian vehicle numbers
        val regex = "^[A-Z]{2}\\d{1,2}[A-Z]{1,2}\\d{4}$".toRegex(RegexOption.IGNORE_CASE)
        return regex.matches(text)
    }


    /**
     * Formats vehicle number by removing all spaces
     * Example: "MH 03 CM6 156" -> "MH03CM6156"
     */

    private fun formatVehicleNumberOLD(vehicleNumber: String): String {
        // Simply remove all whitespace characters from the input
        return vehicleNumber.replace("\\s".toRegex(), "")
    }
}