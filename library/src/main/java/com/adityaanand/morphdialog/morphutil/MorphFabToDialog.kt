package com.adityaanand.morphdialog.morphutil

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionValues
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.adityaanand.morphdialog.R


/**
 * A transition that morphs a circle into a rectangle, changing it's background color.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class MorphFabToDialog : ChangeBounds {
    internal var backgroundColor: Int = 0

    constructor(backgroundColor: Int) : super() {
        this.backgroundColor = backgroundColor
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun getTransitionProperties(): Array<String> {
        return TRANSITION_PROPERTIES
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        super.captureStartValues(transitionValues)
        val view = transitionValues.view
        if (view.width <= 0 || view.height <= 0) {
            return
        }
        transitionValues.values[PROPERTY_COLOR] = fetchAccentColor(view.context)
        transitionValues.values[PROPERTY_CORNER_RADIUS] = view.height / 2//view.getHeight() / 2
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        super.captureEndValues(transitionValues)
        val view = transitionValues.view
        if (view.width <= 0 || view.height <= 0) {
            return
        }
        transitionValues.values[PROPERTY_COLOR] = backgroundColor
        transitionValues.values[PROPERTY_CORNER_RADIUS] = view.resources.getDimensionPixelSize(R.dimen.default_dialog_corners)
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        val changeBounds = super.createAnimator(sceneRoot, startValues, endValues)
        if (startValues == null || endValues == null || changeBounds == null) {
            return null
        }

        val startColor = startValues.values[PROPERTY_COLOR] as Int?
        val startCornerRadius = startValues.values[PROPERTY_CORNER_RADIUS] as Int?
        val endColor = endValues.values[PROPERTY_COLOR] as Int?
        val endCornerRadius = endValues.values[PROPERTY_CORNER_RADIUS] as Int?

        if (startColor == null || startCornerRadius == null || endColor == null || endCornerRadius == null) {
            return null
        }

        val background = MorphDrawable(startColor, startCornerRadius.toFloat())
        endValues.view.background = background

        val color = ObjectAnimator.ofArgb(background, MorphDrawable.COLOR, endColor)
        val corners = ObjectAnimator.ofFloat<MorphDrawable>(background, MorphDrawable.CORNER_RADIUS, endCornerRadius.toFloat())

        // ease in the dialog's child views (slide up & fade in)
        if (endValues.view is ViewGroup) {
            val vg = endValues.view as ViewGroup
            var offset = (vg.height / 3).toFloat()
            for (i in 0 until vg.childCount) {
                val v = vg.getChildAt(i)
                v.translationY = offset
                v.alpha = 0f
                v.animate().alpha(1f).translationY(0f).setDuration(150).setStartDelay(150)
                        .setInterpolator(AnimationUtils.loadInterpolator(vg.context, android.R.interpolator.fast_out_slow_in))
                        .start()
                offset *= 1.8f
            }
        }

        val transition = AnimatorSet()
        transition.playTogether(changeBounds, corners, color)
        transition.duration = 300
        transition.interpolator = AnimationUtils.loadInterpolator(sceneRoot.context, android.R.interpolator.fast_out_slow_in)
        return transition
    }

    companion object {

        private val PROPERTY_COLOR = "circleMorph:color"
        private val PROPERTY_CORNER_RADIUS = "circleMorph:cornerRadius"
        private val TRANSITION_PROPERTIES = arrayOf(PROPERTY_COLOR, PROPERTY_CORNER_RADIUS)

        fun fetchAccentColor(context: Context): Int {
            val typedValue = TypedValue()

            val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
            val color = a.getColor(0, 0)

            a.recycle()

            return color
        }
    }
}
