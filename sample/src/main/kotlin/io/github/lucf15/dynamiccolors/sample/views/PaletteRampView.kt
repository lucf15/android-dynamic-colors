/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.graphics.ColorUtils
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.TonalRamp
import io.github.lucf15.dynamiccolors.Tone
import io.github.lucf15.dynamiccolors.sample.model.*
import io.github.lucf15.dynamiccolors.sample.theme.AppColors
import io.github.lucf15.dynamiccolors.sample.views.components.*

/** Native rendering of one complete ramp; the containing list recycles families independently. */
internal class PaletteRampView(context: Context) : LinearLayout(context) {
    private var previous: Pair<PaletteFamily, TonalRamp>? = null
    private val bindings = mutableListOf<(AppColors) -> Unit>()

    init {
        orientation = VERTICAL
    }

    fun update(family: PaletteFamily, ramp: TonalRamp, colors: AppColors) {
        val next = family to ramp
        if (previous == next) {
            bindings.forEach { it(colors) }
            return
        }
        previous = next
        removeAllViews()
        bindings.clear()
        val card = StudioCardView(context).apply { bindings += { colors(it) } }
        card.addView(
            StudioTextView(context, family.title, 15, 600).apply {
                bindings += { setTextColor(it.text) }
            },
        )
        Tone.entries.chunked(4).forEach { tones ->
            card.addView(View(context), LayoutParams(1, context.px(18)))
            val row = StudioRow(context)
            repeat(4) { index ->
                val tile =
                    if (index < tones.size) swatch(family, tones[index], ramp[tones[index]])
                    else View(context)
                row.addView(
                    tile,
                    LayoutParams(0, -2, 1f).apply { if (index < 3) marginEnd = context.px(8) },
                )
            }
            card.addView(row, LayoutParams(-1, -2))
        }
        addView(card, LayoutParams(-1, -2))
        bindings.forEach { it(colors) }
    }

    private fun swatch(family: PaletteFamily, tone: Tone, argb: Int): View =
        LinearLayout(context).apply {
            orientation = VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            contentDescription =
                "${family.title}, tone ${tone.hctTone}, ${argb.rgbHex()}, ${family.resourceName(tone)}"
            isFocusable = true
            NativeMotion.press(this)
            setOnClickListener { copyPaletteColor(context, family, tone, argb) }
            addView(
                StudioTextView(context, "${tone.hctTone}", 12, 500).apply {
                    gravity = Gravity.CENTER
                    bindings += { background = context.studioShape(argb, it.outline, 12) }
                    setTextColor(
                        if (ColorUtils.calculateLuminance(argb) > 0.179) Color.BLACK
                        else Color.WHITE,
                    )
                    importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
                },
                LayoutParams(-1, context.px(44)),
            )
            addView(View(context), LayoutParams(1, context.px(6)))
            addView(
                StudioTextView(context, argb.rgbHex(), 11).apply {
                    bindings += { setTextColor(it.muted) }
                    importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
                },
                LayoutParams(-2, -2),
            )
        }
}
