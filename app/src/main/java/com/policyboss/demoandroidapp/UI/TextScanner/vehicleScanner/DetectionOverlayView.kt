package com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DetectionOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Pre-allocate Paint object with thicker borderx
    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 6f  // Thicker border for better visibility
    }

    // Pre-allocate RectF to avoid creating it in onDraw
    private val viewRect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
x
        // Calculate rectangle that's 80% of width with aspect ratio for license plates
        val rectWidth = width * 0.8f
        val rectHeight = rectWidth * 0.2f  // License plate aspect ratio (approximately 5:1)

        // Calculate centered position
        val left = (width - rectWidth) / 2
        val top = (height - rectHeight) / 2

        // Set the rectangle bounds
        viewRect.set(left, top, left + rectWidth, top + rectHeight)

        // Draw the rectangle
        canvas.drawRect(viewRect, rectPaint)
    }

    // This method returns the detection rectangle in actual image coordinates
    fun getDetectionRectInImageCoordinates(imageWidth: Int, imageHeight: Int): Rect {
        // Calculate rectangle that's 80% of width with aspect ratio for license plates
        val rectWidth = width * 0.8f
        val rectHeight = rectWidth * 0.2f  // Same as in onDraw

        // Calculate centered position
        val left = (width - rectWidth) / 2
        val top = (height - rectHeight) / 2

        // Calculate the scaling factors
        val scaleX = imageWidth.toFloat() / width
        val scaleY = imageHeight.toFloat() / height

        // Convert view coordinates to image coordinates
        return Rect(
            (left * scaleX).toInt(),
            (top * scaleY).toInt(),
            ((left + rectWidth) * scaleX).toInt(),
            ((top + rectHeight) * scaleY).toInt()
        )
    }
}