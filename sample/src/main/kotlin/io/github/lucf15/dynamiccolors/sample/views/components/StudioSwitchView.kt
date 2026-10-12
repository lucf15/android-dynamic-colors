/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import io.github.lucf15.dynamiccolors.sample.theme.AppColors

internal class StudioSwitchView(context: Context, private val onChanged: (Boolean) -> Unit) :
    FrameLayout(context) {
    private val track = FrameLayout(context)
    private val thumb = View(context)
    private var checked = true

    init {
        minimumWidth = context.px(48)
        minimumHeight = context.px(48)
        addView(track, LayoutParams(context.px(44), context.px(26), Gravity.CENTER))
        track.addView(
            thumb,
            LayoutParams(
                    context.px(20),
                    minOf(context.px(20), context.px(26) - 2 * context.px(3)),
                )
                .apply {
                    leftMargin = context.px(3)
                    topMargin = context.px(3)
                },
        )
        contentDescription = "Notifications"
        isFocusable = true
        setOnClickListener { onChanged(!checked) }
    }

    fun update(value: Boolean, c: AppColors) {
        val changed = checked != value
        checked = value
        track.background = context.studioShape(if (value) c.accent else c.outline, radius = 13)
        thumb.background = context.studioShape(if (value) c.onAccent else c.surface, radius = 10)
        val x = if (value) context.px(18).toFloat() else 0f
        if (changed && isLaidOut) NativeMotion.spring(thumb, DynamicAnimation.TRANSLATION_X, x)
        else if (!isLaidOut) thumb.translationX = x
    }

    @Suppress(
        "DEPRECATION",
    ) // Boolean checked state is supported on the sample’s older Android versions.
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = "android.widget.Switch"
        info.isCheckable = true
        info.isChecked = checked
    }
}
