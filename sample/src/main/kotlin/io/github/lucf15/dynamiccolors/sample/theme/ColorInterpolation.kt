/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.theme

import androidx.core.graphics.ColorUtils

internal fun AppColors.interpolate(other: AppColors, fraction: Float) =
    AppColors(
        background = ColorUtils.blendARGB(background, other.background, fraction),
        surface = ColorUtils.blendARGB(surface, other.surface, fraction),
        text = ColorUtils.blendARGB(text, other.text, fraction),
        muted = ColorUtils.blendARGB(muted, other.muted, fraction),
        accent = ColorUtils.blendARGB(accent, other.accent, fraction),
        onAccent = ColorUtils.blendARGB(onAccent, other.onAccent, fraction),
        outline = ColorUtils.blendARGB(outline, other.outline, fraction),
        accentSurface = ColorUtils.blendARGB(accentSurface, other.accentSurface, fraction),
        onAccentSurface = ColorUtils.blendARGB(onAccentSurface, other.onAccentSurface, fraction),
        secondary = ColorUtils.blendARGB(secondary, other.secondary, fraction),
        error = ColorUtils.blendARGB(error, other.error, fraction),
        errorSurface = ColorUtils.blendARGB(errorSurface, other.errorSurface, fraction),
        onErrorSurface = ColorUtils.blendARGB(onErrorSurface, other.onErrorSurface, fraction),
    )
