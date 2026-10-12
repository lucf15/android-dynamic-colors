/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.EditText
import io.github.lucf15.dynamiccolors.sample.R
import io.github.lucf15.dynamiccolors.sample.theme.AppColors

internal class StudioFieldView(context: Context) : EditText(context) {
    init {
        hint = "Collection name"
        contentDescription = "Collection name"
        setSingleLine()
        gravity = Gravity.TOP or Gravity.START
        textSize = 15f
        typeface = androidx.core.content.res.ResourcesCompat.getFont(context, R.font.inter)
        includeFontPadding = false
        setPadding(context.px(16), context.px(16), context.px(16), context.px(16))
        minimumHeight = context.px(53)
    }

    fun colors(c: AppColors) {
        setTextColor(c.text)
        setHintTextColor(c.muted)
        background = context.studioShape(c.surface, if (hasFocus()) c.accent else c.outline, 14)
        if (Build.VERSION.SDK_INT >= 29) textCursorDrawable?.setTint(c.accent)
    }
}
