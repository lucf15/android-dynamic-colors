/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.os.Build
import android.view.MotionEvent
import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import io.github.lucf15.dynamiccolors.sample.R

internal object NativeMotion {
    fun spring(view: View, property: DynamicAnimation.ViewProperty, target: Float) {
        @Suppress("UNCHECKED_CAST")
        val animations =
            view.getTag(R.id.studio_motion)
                as? MutableMap<DynamicAnimation.ViewProperty, SpringAnimation>
                ?: mutableMapOf<DynamicAnimation.ViewProperty, SpringAnimation>().also { map ->
                    view.setTag(R.id.studio_motion, map)
                    view.addOnAttachStateChangeListener(
                        object : View.OnAttachStateChangeListener {
                            override fun onViewAttachedToWindow(v: View) = Unit

                            override fun onViewDetachedFromWindow(v: View) {
                                map.values.forEach { it.cancel() }
                            }
                        },
                    )
                }
        if (Build.VERSION.SDK_INT >= 26 && !android.animation.ValueAnimator.areAnimatorsEnabled()) {
            animations[property]?.cancel()
            property.setValue(view, target)
            return
        }
        val animation =
            animations.getOrPut(property) {
                SpringAnimation(view, property).apply {
                    spring = SpringForce().setDampingRatio(0.9f).setStiffness(600f)
                }
            }
        animation.animateToFinalPosition(target)
    }

    fun press(view: View) {
        view.setOnTouchListener { v, event ->
            if (v.isEnabled) {
                val down =
                    event.actionMasked == MotionEvent.ACTION_DOWN ||
                        (event.actionMasked == MotionEvent.ACTION_MOVE &&
                            event.x in 0f..v.width.toFloat() &&
                            event.y in 0f..v.height.toFloat())
                spring(v, DynamicAnimation.SCALE_X, if (down) 0.985f else 1f)
                spring(v, DynamicAnimation.SCALE_Y, if (down) 0.985f else 1f)
            }
            false // Native click, cancellation, focus, and accessibility handling remain intact.
        }
    }
}
