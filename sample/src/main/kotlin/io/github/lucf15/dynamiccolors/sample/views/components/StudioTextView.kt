/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
import android.widget.TextView
import io.github.lucf15.dynamiccolors.sample.R

internal open class StudioTextView(
    context: Context,
    value: String = "",
    size: Int = 15,
    weight: Int = 400,
) : TextView(context) {
    init {
        text = value
        textSize = size.toFloat()
        if (size >= 28) letterSpacing = -0.6f / size
        typeface = androidx.core.content.res.ResourcesCompat.getFont(context, R.font.inter)
        if (Build.VERSION.SDK_INT >= 26) fontVariationSettings = "'wght' $weight"
        else if (weight >= 600) setTypeface(typeface, Typeface.BOLD)
        includeFontPadding = false
        gravity = Gravity.CENTER_VERTICAL
        val line =
            android.util.TypedValue.applyDimension(
                android.util.TypedValue.COMPLEX_UNIT_SP,
                size * 1.4f,
                resources.displayMetrics,
            )
        setLineSpacing(line - (paint.fontMetrics.descent - paint.fontMetrics.ascent), 1f)
    }

    override fun onDraw(canvas: Canvas) {
        val free = height - compoundPaddingTop - compoundPaddingBottom - (layout?.height ?: 0)
        val shift =
            if (gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.CENTER_VERTICAL && free > 0)
                free % 2
            else 0
        canvas.save()
        val textWidth =
            layout
                ?.takeIf { it.lineCount == 1 }
                ?.getLineWidth(0)
                ?.let { kotlin.math.ceil(it).toInt() }
        val horizontalFree =
            width - compoundPaddingLeft - compoundPaddingRight - (textWidth ?: width)
        val horizontalShift =
            if (
                gravity and Gravity.HORIZONTAL_GRAVITY_MASK == Gravity.CENTER_HORIZONTAL &&
                    horizontalFree > 0
            )
                horizontalFree % 2
            else 0
        canvas.translate(horizontalShift.toFloat(), shift.toFloat())
        super.onDraw(canvas)
        canvas.restore()
    }
}
