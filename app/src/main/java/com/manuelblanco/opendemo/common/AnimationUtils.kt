package com.manuelblanco.opendemo.common

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.manuelblanco.opendemo.R
import kotlin.math.roundToInt

object AnimationUtils {

    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {
        textview.clearAnimation()
        val decelerateInterpolator = DecelerateInterpolator(0.8f)
        val start = Math.min(initialValue, finalValue)
        val end = Math.max(initialValue, finalValue)
        val difference = Math.abs(finalValue - initialValue)
        val handler = Handler()
        if (finalValue > initialValue) {
            var value = 0
            for (count in start..end) {
                val time: Int =
                    (decelerateInterpolator.getInterpolation(value.toFloat() / difference) * 100).roundToInt() * value
                val finalCount = if (initialValue > finalValue) initialValue - count else count
                handler.postDelayed(
                    { textview.setText(finalCount.toString()) },
                    time.toLong()
                )
                value++
            }
        }
    }

    fun animateScaleText(context: Context, textview: TextView) {
        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.scale)
        animation.reset()
        textview.clearAnimation()
        textview.startAnimation(animation)
    }

    fun animateCrossFadeText(context: Context, textview: TextView) {
        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.scroll_animation)
        animation.reset()
        textview.clearAnimation()
        textview.startAnimation(animation)
    }

    fun animateFab(isSelected: Boolean, fab: FloatingActionButton, context: Context) {
        ObjectAnimator.ofFloat(fab, "rotation", 0f, 360f).setDuration(800).start()
        val handler = Handler()
        handler.postDelayed({
            if (isSelected) {
                fab.setImageDrawable(context.resources.getDrawable(R.drawable.ic_baseline_favorite_48))
            } else {
                fab.setImageDrawable(context.resources.getDrawable(R.drawable.ic_baseline_favorite_border_48))
            }
        }, 400)
    }
}