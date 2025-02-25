package com.policyboss.demoandroidapp.UI.TextScanner.scanner

import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


// TextAnalyzer.kt
class TextAnalyzer(
    private val scanOverlay: ScanOverlayView,
    private val onTextDetected: (String) -> Unit  // Changed to simple String parameter
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var lastProcessedText: String? = null
    private var lastProcessedTime: Long = 0
    private val processingCooldown = 500L
    private var consecutiveMatches = 0
    private val requiredMatches = 2

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastProcessedTime < processingCooldown) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            val scanArea = getScanArea(
                scanOverlay.getScanRect(),
                image.width,
                image.height
            )

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    findTextInScanArea(visionText, scanArea)?.let { detectedText ->
                        if (detectedText == lastProcessedText) {
                            consecutiveMatches++
                            if (consecutiveMatches >= requiredMatches) {
                                lastProcessedTime = currentTime
                                onTextDetected(detectedText)  // Return simple String
                                consecutiveMatches = 0
                            }
                        } else {
                            lastProcessedText = detectedText
                            consecutiveMatches = 1
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("TextAnalyzer", "Recognition failed", it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    private fun findTextInScanArea(visionText: Text, scanArea: RectF): String? {
        val blocksInArea = visionText.textBlocks
            .filter { block ->
                val boundingBox = block.boundingBox ?: return@filter false
                calculateOverlap(boundingBox, scanArea) > 0.8f
            }
            .sortedBy { it.boundingBox?.top }
            .take(2)

        if (blocksInArea.isEmpty()) return null

        return blocksInArea.joinToString(" ") { it.text.trim() }
    }

    private fun calculateOverlap(rect: Rect, scanArea: RectF): Float {
        val rectF = RectF(
            rect.left.toFloat(),
            rect.top.toFloat(),
            rect.right.toFloat(),
            rect.bottom.toFloat()
        )

        val intersection = RectF()
        if (!intersection.setIntersect(rectF, scanArea)) {
            return 0f
        }

        val intersectionArea = intersection.width() * intersection.height()
        val rectArea = rectF.width() * rectF.height()

        return intersectionArea / rectArea
    }

    private fun getScanArea(viewRect: RectF, imageWidth: Int, imageHeight: Int): RectF {
        return RectF(
            viewRect.left * imageWidth / scanOverlay.width,
            viewRect.top * imageHeight / scanOverlay.height,
            viewRect.right * imageWidth / scanOverlay.width,
            viewRect.bottom * imageHeight / scanOverlay.height
        )
    }
}

