package com.example.todoapp.other

import android.animation.ValueAnimator
import com.warkiz.widget.IndicatorSeekBar

object ViewUtilities {

    fun animateCustomProgress(
        percentageIndicator: IndicatorSeekBar,
        animationStart: Float,
        animationEnd: Float
    ) {
        val valueAnimator = ValueAnimator.ofFloat(animationStart, animationEnd)
        valueAnimator.duration = 1500
        valueAnimator.addUpdateListener { animation ->
            val animationProgress = animation.animatedValue as Float
            percentageIndicator.setProgress(animationProgress)
        }
        valueAnimator.start()
    }

    fun validateInput(title: String, description: String): Boolean {
        return title.trim().isNotEmpty() && description.trim().isNotEmpty()
    }
}