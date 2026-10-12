/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.graphics.drawable.GradientDrawable

internal fun Context.px(dp: Int): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()

internal fun Context.studioShape(fill: Int, border: Int = fill, radius: Int = 20) =
    GradientDrawable().apply {
        setColor(fill)
        cornerRadius = px(radius).toFloat()
        if (border != fill) setStroke(px(1), border)
    }
