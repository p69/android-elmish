package com.example.pavelshilyagov.tryelmish.elmish

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


fun inAnimator(view: View, parentsWidth:Float): Animator {
    val startPosition = 0.0f - parentsWidth
    val endPosition = 0.0f
    return ObjectAnimator.ofFloat(view, View.X, startPosition, endPosition)
}

fun outAnimator(view: View, parentsWidth:Float): Animator {
    val startPosition = 0.0f
    val endPosition = 2.0f*parentsWidth
    return ObjectAnimator.ofFloat(view, View.X,  startPosition, endPosition)
}