package com.policyboss.demoandroidapp.UI.TextRecognizer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceView

class CustomSurfaceView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        val left = 50f
        val top = 50f
        val right = 200f
        val bottom = 200f
        canvas.drawRect(left, top, right, bottom, paint)
    }
}