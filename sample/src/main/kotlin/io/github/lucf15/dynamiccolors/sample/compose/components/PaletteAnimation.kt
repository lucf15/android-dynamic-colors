/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import io.github.lucf15.dynamiccolors.sample.theme.StudioColors

@Composable
internal fun animatedPalette(target: StudioColors): StudioColors {
    @Composable
    fun tone(value: Color): Color = animateColorAsState(value, tween(260), label = "Palette").value
    return StudioColors(
        tone(target.background),
        tone(target.surface),
        tone(target.text),
        tone(target.muted),
        tone(target.accent),
        tone(target.onAccent),
        tone(target.outline),
        tone(target.accentSurface),
        tone(target.onAccentSurface),
        tone(target.secondary),
        tone(target.error),
        tone(target.errorSurface),
        tone(target.onErrorSurface),
    )
}
