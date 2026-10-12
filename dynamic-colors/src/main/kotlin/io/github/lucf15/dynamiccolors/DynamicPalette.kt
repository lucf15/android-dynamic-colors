/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

/** The platform's six tonal families. Error resources exist only from API 35. */
public enum class PaletteFamily {
    Accent1,
    Accent2,
    Accent3,
    Neutral1,
    Neutral2,
    Error,
}

/**
 * Raw system ramps for the supplied Context's configuration. Android 17 can resolve different
 * colors in day and night configurations; this is a snapshot of one configuration, not both.
 * [error] is absent before API 35. No tones are synthesized and no fallback is substituted.
 */
public data class DynamicPalette(
    public val accent1: TonalRamp,
    public val accent2: TonalRamp,
    public val accent3: TonalRamp,
    public val neutral1: TonalRamp,
    public val neutral2: TonalRamp,
    public val error: TonalRamp? = null,
) {
    public operator fun get(family: PaletteFamily): TonalRamp? =
        when (family) {
            PaletteFamily.Accent1 -> accent1
            PaletteFamily.Accent2 -> accent2
            PaletteFamily.Accent3 -> accent3
            PaletteFamily.Neutral1 -> neutral1
            PaletteFamily.Neutral2 -> neutral2
            PaletteFamily.Error -> error
        }
}
