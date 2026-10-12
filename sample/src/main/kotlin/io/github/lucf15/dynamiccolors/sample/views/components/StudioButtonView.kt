/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.view.Gravity
import io.github.lucf15.dynamiccolors.sample.theme.AppColors

internal class StudioButtonView(
    context: Context,
    text: String,
    private val filled: Boolean = true,
) : StudioTextView(context, text, 15, 600) {
    init {
        gravity = Gravity.CENTER
        minimumHeight = context.px(50)
        setPadding(context.px(20), context.px(14), context.px(20), context.px(14))
        isFocusable = true
        NativeMotion.press(this)
    }

    fun colors(c: AppColors) {
        background =
            context.studioShape(
                if (filled) c.accent else c.surface,
                if (filled) c.accent else c.outline,
                25,
            )
        setTextColor(if (filled) c.onAccent else c.text)
        alpha = if (isEnabled) 1f else 0.38f
    }

    override fun getAccessibilityClassName(): CharSequence = "android.widget.Button"
}
