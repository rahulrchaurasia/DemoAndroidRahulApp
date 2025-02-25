package com.policyboss.demoandroidapp.UI.TextScanner.scanner

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.policyboss.demoandroidapp.databinding.ActivityTextScannerBinding

import android.Manifest // Use this specific import
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.MotionEvent
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.TorchState
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import java.util.concurrent.TimeUnit


class TextScannerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityTextScannerBinding
    //private lateinit var previewView: PreviewView

    private lateinit var previewView: PreviewView
    private lateinit var scanOverlay: ScanOverlayView
    private lateinit var resultView: ResultView
    private var camera: Camera? = null
    private var isProcessing = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTextScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewView = binding.viewFinder
        scanOverlay =  binding.scanOverlay
        resultView =   binding.resultView

        // Set up touch focus


        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }



    }



    private fun focusOnPoint(x: Float, y: Float) {
        val factory = SurfaceOrientedMeteringPointFactory(
            previewView.width.toFloat(),
            previewView.height.toFloat()
        )
        val point = factory.createPoint(x, y)
        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
            .setAutoCancelDuration(2, TimeUnit.SECONDS)
            .build()
        camera?.cameraControl?.startFocusAndMetering(action)
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Camera selector
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            // Preview use case
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Image analysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        ContextCompat.getMainExecutor(this),
                        TextAnalyzer(scanOverlay) { text ->
                            showResult(text)
                        }
                    )
                }

            // Camera control
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageAnalysis
                ).also { camera ->
                    // Set up camera controls
                    setupCameraControls(camera)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // Update setupCameraControls in TextScannerActivity.kt
    private fun setupCameraControls(camera: Camera) {
        try {
            // Set auto-focus mode
            camera.cameraControl.cancelFocusAndMetering()

            val factory = SurfaceOrientedMeteringPointFactory(1f, 1f)
            val centerPoint = factory.createPoint(0.5f, 0.5f)

            val action = FocusMeteringAction.Builder(
                centerPoint,
                FocusMeteringAction.FLAG_AF or    // Auto Focus
                        FocusMeteringAction.FLAG_AE or    // Auto Exposure
                        FocusMeteringAction.FLAG_AWB      // Auto White Balance
            ).apply {
                // Enable auto-cancel to allow continuous refocus
                setAutoCancelDuration(2, TimeUnit.SECONDS)
            }.build()

            camera.cameraControl.startFocusAndMetering(action)

            // Enable torch only in low light conditions
            camera.cameraInfo.torchState.observe(this) { torchState ->
                if (torchState == TorchState.OFF) {
                    camera.cameraControl.enableTorch(true)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to setup camera controls", e)
        }
    }


    private fun showResult(text: String) {
        if (!isProcessing) {
            isProcessing = true

            // Check if it's a vehicle number
            if (isVehicleNumber(text)) {
                Toast.makeText(
                    this,
                    "Valid Vehicle Number Detected",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Update ResultView
            resultView.apply {
                this.text = text
                visibility = View.VISIBLE
                alpha = 0f
                animate()
                    .alpha(1f)
                    .setDuration(300)
                    .withEndAction {
                        postDelayed({
                            visibility = View.GONE
                            isProcessing = false
                        }, 2000)
                    }
                    .start()
            }
        }
    }

    // Add this function to TextScannerActivity
    private fun isVehicleNumber(text: String): Boolean {
        val cleanText = text.replace("[\\s-]".toRegex(), "")

        // Vehicle number patterns
        return cleanText.matches(Regex("^[A-Z]{2}\\d{2}[A-Z]{1,2}\\d{4}[A-Z]?$")) || // New format
                cleanText.matches(Regex("^[A-Z]{2}\\d{4}$")) // Old format
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        baseContext,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val TAG = "ScannerActivity"
        private const val CAMERA_PERMISSION_REQUEST = 101
    }
}