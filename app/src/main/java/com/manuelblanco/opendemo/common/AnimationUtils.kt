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

    /**
     * Animation for FAB button on detail screen.
     */
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