/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

import androidx.annotation.ColorInt

/**
 * An immutable ramp with all thirteen [Tone] stops. Construct with explicit colors for previews,
 * tests, or your own palette. The callback is evaluated exactly once per tone.
 */
public class TonalRamp(color: (Tone) -> Int) {
    private val colors: IntArray = IntArray(Tone.entries.size) { color(Tone.entries[it]) }

    @ColorInt public operator fun get(tone: Tone): Int = colors[tone.ordinal]

    override fun equals(other: Any?): Boolean =
        this === other || (other is TonalRamp && colors.contentEquals(other.colors))

    override fun hashCode(): Int = colors.contentHashCode()

    override fun toString(): String =
        "TonalRamp(${Tone.entries.joinToString { "$it=#${get(it).toUInt().toString(16).padStart(8, '0')}" }})"
}
