package com.example.attempt

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.TextPaint
import android.text.style.ReplacementSpan

class GradientText(private val colors: IntArray) : ReplacementSpan() {

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return paint.measureText(text, start, end).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val shader = LinearGradient(0f, 0f, 0f, paint.textSize, colors, null, Shader.TileMode.CLAMP)
        val textPaint = TextPaint(paint)
        textPaint.shader = shader
        canvas.drawText(text!!, start, end, x, y.toFloat(), textPaint)
    }
}
