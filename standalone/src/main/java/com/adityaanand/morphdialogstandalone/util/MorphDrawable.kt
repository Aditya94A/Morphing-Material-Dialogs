package com.adityaanand.morphdialogstandalone.util

import android.annotation.TargetApi
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Property
import androidx.annotation.ColorInt

/**
 * A drawable that can morph size, shape (via it's corner radius) and color.  Specifically this is
 * useful for animating between a FAB and a dialog.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class MorphDrawable(@ColorInt color: Int, private var cornerRadius: Float) : Drawable() {

    private val paint: Paint

    var color: Int
        get() = paint.color
        set(color) {
            paint.color = color
            invalidateSelf()
        }

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
    }

    fun getCornerRadius(): Float {
        return cornerRadius
    }

    fun setCornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds
                .bottom.toFloat(), cornerRadius, cornerRadius, paint)//hujiawei
    }

    override fun getOutline(outline: Outline) {
        outline.setRoundRect(bounds, cornerRadius)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return paint.alpha
    }

    companion object {

        val CORNER_RADIUS: Property<MorphDrawable, Float> = object : Property<MorphDrawable, Float>(Float::class.java, "cornerRadius") {

            override fun set(morphDrawable: MorphDrawable, value: Float?) {
                morphDrawable.setCornerRadius(value!!)
            }

            override fun get(morphDrawable: MorphDrawable): Float {
                return morphDrawable.getCornerRadius()
            }
        }

        val COLOR: Property<MorphDrawable, Int> = object : Property<MorphDrawable, Int>(Int::class.java, "color") {

            override fun set(morphDrawable: MorphDrawable, value: Int) {
                morphDrawable.color = value
            }

            override fun get(morphDrawable: MorphDrawable): Int {
                return morphDrawable.color
            }
        }
    }
}
