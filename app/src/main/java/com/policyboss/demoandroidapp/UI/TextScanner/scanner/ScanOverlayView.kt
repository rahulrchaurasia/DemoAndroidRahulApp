package com.policyboss.demoandroidapp.UI.TextScanner.scanner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.camera.core.Camera
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import com.policyboss.demoandroidapp.Constant
import java.util.concurrent.TimeUnit

// ScanOverlayView.kt - Enhanced for clearer boundaries
class ScanOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f // Thicker border for better visibility
    }

    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#99000000") // Darker overlay (more opaque)
        style = Paint.Style.FILL
    }

    private val highlightPaint = Paint().apply {
        color = Color.parseColor("#2000FFFF") // Light blue highlight
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 36f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val scanRect = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Adjust scan area to be slightly narrower but clearer
        val rectWidth = width * 0.7f
        val rectHeight = rectWidth * 0.3f // Good aspect ratio for single line text
        val left = (width - rectWidth) / 2
        val top = (height - rectHeight) / 2
        scanRect.set(left, top, left + rectWidth, top + rectHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw dark overlay (darkened outside scan area)
        canvas.drawRect(0f, 0f, width.toFloat(), scanRect.top, overlayPaint)
        canvas.drawRect(0f, scanRect.bottom, width.toFloat(), height.toFloat(), overlayPaint)
        canvas.drawRect(0f, scanRect.top, scanRect.left, scanRect.bottom, overlayPaint)
        canvas.drawRect(scanRect.right, scanRect.top, width.toFloat(), scanRect.bottom, overlayPaint)

        // Draw highlight in scan area
        canvas.drawRect(scanRect, highlightPaint)

        // Draw scanning rectangle with thick border
        canvas.drawRect(scanRect, rectPaint)

        // Draw guide text below scan area
        canvas.drawText(
            "Please bring number plate in this view",
            width / 2f,
            scanRect.bottom + 60f,
            textPaint
        )
    }

    fun getScanRect(): RectF = scanRect
}

// In TextScannerActivity.kt - Update setupCameraControls
private fun setupCameraControls(camera: Camera) {
    try {
        // Disable torch by default - it can cause glare on screens
        camera.cameraControl.enableTorch(false)

        // Configure auto-focus with continuous picture mode
        camera.cameraControl.cancelFocusAndMetering()

        val factory = SurfaceOrientedMeteringPointFactory(1f, 1f)
        val centerPoint = factory.createPoint(0.5f, 0.5f)

        val action = FocusMeteringAction.Builder(
            centerPoint,
            FocusMeteringAction.FLAG_AF or    // Auto Focus
                    FocusMeteringAction.FLAG_AE       // Auto Exposure
        ).apply {
            // Keep focus active longer without canceling
            setAutoCancelDuration(5, TimeUnit.SECONDS)
        }.build()

        camera.cameraControl.startFocusAndMetering(action)
    } catch (e: Exception) {
        Log.e(Constant.TAG, "Failed to setup camera controls", e)
    }
}