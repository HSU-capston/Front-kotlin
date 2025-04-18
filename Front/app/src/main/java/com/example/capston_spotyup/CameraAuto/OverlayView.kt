package com.example.tflitetestapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class OverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var objects: List<DrawObject> = listOf()
    var previewBitmap: Bitmap? = null

    private val boxPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val textPaint = Paint().apply {
        color = Color.YELLOW
        textSize = 40f
        typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        previewBitmap?.let {
//            canvas.drawBitmap(it, 0f, 0f, null)
//        }
//        for (obj in objects) {
//            val (left, top, right, bottom) = obj.box
//            canvas.drawRect(left, top, right, bottom, boxPaint)
////            Log.d("left = $left, top = $top, right = $right")
//            canvas.drawText("${obj.label} %.2f".format(obj.score), left, top - 10, textPaint)
//        }
//        previewBitmap?.let {
//            // 실제 프리뷰화면을 표시할 필요가 없다면 생략 가능
//            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
//        }

        for (obj in objects) {
            val (leftNorm, topNorm, rightNorm, bottomNorm) = obj.box

            val left = leftNorm * width
            val top = topNorm * height
            val right = rightNorm * width
            val bottom = bottomNorm * height

            canvas.drawRect(left, top, right, bottom, boxPaint)
            canvas.drawText("${obj.label} %.2f".format(obj.score), left, top - 10, textPaint)
        }
    }
}
