package com.policyboss.demoandroidapp.UI.TextRecognizer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ScanOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }

    private var text: String? = null

    fun setText(text: String?) {
        this.text = text
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.drawRect(
                (width / 2) - 200f,
                (height / 2) - 100f,
                (width / 2) + 200f,
                (height / 2) + 100f,
                paint
            )

            text?.let { text ->
                it.drawText(
                    text,
                    width / 2f,
                    (height / 2f) + ((textPaint.descent() + textPaint.ascent()) / 2f),
                    textPaint
                )
            }

        }
    }
}
