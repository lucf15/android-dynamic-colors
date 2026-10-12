/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.widget.LinearLayout
import io.github.lucf15.dynamiccolors.sample.theme.AppColors

internal class StudioCardView(context: Context) : LinearLayout(context) {
    init {
        orientation = VERTICAL
        setPadding(context.px(20), context.px(20), context.px(20), context.px(20))
    }

    fun colors(c: AppColors) {
        background = context.studioShape(c.surface, c.outline, 22)
    }
}
