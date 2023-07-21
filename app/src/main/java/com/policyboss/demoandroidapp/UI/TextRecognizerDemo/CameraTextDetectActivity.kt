package com.policyboss.demoandroidapp.UI.TextRecognizerDemo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Paint
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.TextureView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.policyboss.demoandroidapp.databinding.ActivityCameraTextDetectBinding
import com.policyboss.demoandroidapp.databinding.ActivityTextRecognizerBinding


import android.util.Log
import android.util.Size

import android.view.ViewGroup

import androidx.camera.core.*
import androidx.camera.core.impl.ImageAnalysisConfig
import androidx.camera.core.impl.PreviewConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.android.gms.vision.Frame

import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import com.google.mlkit.vision.text.TextRecognition
import java.nio.ByteBuffer


class CameraTextDetectActivity : AppCompatActivity() {

    lateinit var binding : ActivityCameraTextDetectBinding

    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private lateinit var cameraExecutor: ExecutorService
    private val TAG = "TextScanner"



   


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraTextDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }







}