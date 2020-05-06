package com.example.todoapp.other

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.SharedPreferences
import com.warkiz.widget.IndicatorSeekBar
import javax.inject.Inject

object ViewUtilities {

    const val LAST_PERCENTAGE = "lastPercentage"

    fun animateCustomProgress(
        percentageIndicator: IndicatorSeekBar,
        animationStart: Float,
        animationEnd: Float, sharedPreferences: SharedPreferences
    ) {
        val valueAnimator = ValueAnimator.ofFloat(animationStart, animationEnd)
        valueAnimator.duration = 1500
        valueAnimator.addUpdateListener { animation ->
            val animationProgress = animation.animatedValue as Float
            percentageIndicator.setProgress(animationProgress)
        }
        valueAnimator.start()

        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                val editor = sharedPreferences.edit()
                editor.putFloat(LAST_PERCENTAGE, animationEnd)
                editor.apply()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}

        })
    }

    fun validateInput(title: String, description: String): Boolean {
        return title.trim().isNotEmpty() && description.trim().isNotEmpty()
    }
}