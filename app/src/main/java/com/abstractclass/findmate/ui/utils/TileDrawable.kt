package com.abstractclass.findmate.ui.utils

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log

class TileDrawable(drawable: Drawable, tileMode: Shader.TileMode) : Drawable() {

    private val paint: Paint

    init {
        paint = Paint().apply {
            shader = BitmapShader(getBitmap(drawable), tileMode, tileMode)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPaint(paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        Log.d("TestPish", "drawable widht ${drawable.intrinsicWidth} ${drawable.intrinsicHeight}")

        val bmp = Bitmap.createBitmap(560, 345,
            Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        drawable.setBounds(0, 0, 560, 345)
        drawable.draw(c)
        return bmp
    }

}