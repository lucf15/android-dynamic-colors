/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views

import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import io.github.lucf15.dynamiccolors.sample.views.components.NativeMotion
import io.github.lucf15.dynamiccolors.sample.views.components.px
import io.github.lucf15.dynamiccolors.sample.views.components.studioShape

internal fun NativeShowcase.buildFeedbackSection() {
    heading("Feedback", "Error state")
    error.setPadding(context.px(18), context.px(18), context.px(18), context.px(18))
    error.addView(
        column().apply {
            addView(
                label("Couldn’t save changes", 15, 600).apply {
                    bind { setTextColor(it.onErrorSurface) }
                },
            )
            addView(
                label("Please try again.", 12).apply {
                    bind { setTextColor(it.onErrorSurface) }
                },
            )
        },
        weight(),
    )
    error.addView(
        label("Dismiss", 12, 600).apply {
            isFocusable = true
            accessibilityDelegate =
                object : View.AccessibilityDelegate() {
                    override fun onInitializeAccessibilityNodeInfo(
                        host: View,
                        info: android.view.accessibility.AccessibilityNodeInfo,
                    ) {
                        super.onInitializeAccessibilityNodeInfo(host, info)
                        info.className = "android.widget.Button"
                    }
                }
            setPadding(context.px(12), context.px(12), context.px(12), context.px(12))
            bind { setTextColor(it.onErrorSurface) }
            NativeMotion.press(this)
            setOnClickListener { onChange(state.copy(dismissed = true)) }
        },
    )
    bind {
        error.background = context.studioShape(it.errorSurface, radius = 18)
        restore.colors(it)
    }
    restore.setOnClickListener { onChange(state.copy(dismissed = false)) }
    addView(error, full())
    addView(restore, full())
}
