package com.example.nomenclature.presentation.scanner.qr_code

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View

class BarcodeBoxView(
    context: Context
) : View(context) {
    private val paint = Paint()

    private var mRect = RectF(
        161f,286f,-704f,831f
    )

//    override fun dispatchDraw(canvas: Canvas?) {
//        super.dispatchDraw(canvas)
//        val cornerRadius = 10f
//
//        paint.style = Paint.Style.STROKE
//        paint.color = Color.RED
//        paint.strokeWidth = 10f
//
//        canvas?.drawRoundRect(mRect, cornerRadius, cornerRadius, paint)
//    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.setWillNotDraw(false)

        val cornerRadius = 10f

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 10f

        canvas?.drawRoundRect(mRect, cornerRadius, cornerRadius, paint)
    }

    fun setRect(rect: RectF) {
        //this.setWillNotDraw(false)
        mRect = rect
        invalidate()
        //this.setWillNotDraw(false)
        requestLayout()
    }
}