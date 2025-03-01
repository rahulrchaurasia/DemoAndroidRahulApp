package com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.ui

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
import androidx.activity.enableEdgeToEdge
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.DetectionOverlayView
import com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.PlateDetectionHelper
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.ActivityCarPlateBinding
import com.policyboss.demoandroidapp.databinding.ActivityVehiclePlateReaderBinding

import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class VehiclePlateReaderActivity : AppCompatActivity() {

    lateinit var binding : ActivityVehiclePlateReaderBinding



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

        binding = ActivityVehiclePlateReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize UI components
        cameraPreview = binding.cameraPreview
        plateTextView = binding.plateTextView
        btnDetect = binding.btnDetect
        btnBack =  binding.btnBack
        plateDetectionZone =  binding.plateDetectionZone

        detectionOverlay =  binding.detectionOverlay


        // Setup the detection rectangle once the layout is ready
        // setupDetectionRectangle()
        // Initialize executor for camera operations
        cameraExecutor = Executors.newSingleThreadExecutor()



        //region Listner
        btnDetect.setOnClickListener {

            runOnUiThread {
                val plateText = plateTextView.text.toString()
                if (plateText.isNotEmpty() && plateText != getString(R.string.plate_scan_no_text)) {
                    returnResult(plateText)

                }
            }

        }

        btnBack.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        //endregion

        // regionCheck and request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions.launch(arrayOf(Manifest.permission.CAMERA))
        }
        //endregion
    }


    //region all  method
    //region Open camera and setUp
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
                Log.e(Constant.TAG, "Use case binding failed", e)
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

    //endregion

    //region Processes the Image For Rectangle area detect
    @androidx.camera.core.ExperimentalGetImage
    private fun processImageForPlateDetection(imageProxy: androidx.camera.core.ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // Get detection rectangle from the overlay view in image coordinates
            val imageWidth = imageProxy.width
            val imageHeight = imageProxy.height
            detectionRectangle = detectionOverlay.getDetectionRectInImageCoordinates(imageWidth, imageHeight)

            Log.d(Constant.TAG, "Detection rectangle: $detectionRectangle")

            // Continue with text recognition...
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    processDetectedText(visionText)
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    Log.e(Constant.TAG, "Text recognition failed", e)
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    //endregion

    //region Processes the detect text
    /**
     * Processes detected text to find and format vehicle plate numbers
     */
    private fun processDetectedText(visionText: Text) {

        // Extract text focused on the detection rectangle
        val detectedText = PlateDetectionHelper.getLicensePlateText(visionText,detectionRectangle)


        runOnUiThread {

            if (detectedText.isNotEmpty()) {
                // Look for a valid vehicle number in the detected text

                Log.d(Constant.TAG, "detectedText getLicensePlateText: $detectedText")

                // Extract and clean the vehicle number (remove spaces)
                val vehicleNumber = PlateDetectionHelper.extractVehicleNumber(detectedText)

                if (!vehicleNumber.isNullOrEmpty()) {
                    Log.d(Constant.TAG, "vehicleNumber after extractVehicleNumber: $vehicleNumber")


                    // Format the vehicle number using the helper class
                    val formattedPlate = PlateDetectionHelper.formatVehicleNumber(vehicleNumber)
                    Log.d(Constant.TAG, "Formatted vehicle number: $formattedPlate")

                    // Validate the formatted plate using the helper class
                    val isValid = PlateDetectionHelper.isValidVehicleNumber(formattedPlate)
                    Log.d(Constant.TAG, "Is valid vehicle number? $isValid")


                    if (isValid) {
                        // A valid plate number was found
                        Log.d(Constant.TAG, "Valid vehicle number detected: $formattedPlate")



                        //  update the UI
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
                        // btnDetect.isEnabled = false
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
                   // btnDetect.isEnabled = false
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

    //endregion


    //region set result back to Previous Activity
   /**
    * Returns the detected plate text to the calling activity
    */
    private fun returnResult(plateText: String) {
        val resultIntent = Intent()
        Log.d(Constant.TAG, "Detected Text is $plateText")
        resultIntent.putExtra(Constant.KEY_DETECT_TEXT, plateText.toByteArray(Charsets.UTF_8))
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    //endregion


    //region permission
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
    
    //endregion

    //endregion

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}