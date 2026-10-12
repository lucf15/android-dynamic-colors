/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.sections

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.DynamicPalette
import io.github.lucf15.dynamiccolors.sample.compose.components.Label
import io.github.lucf15.dynamiccolors.sample.compose.components.LocalStudioColors
import io.github.lucf15.dynamiccolors.sample.compose.components.SectionHeading

@Composable
internal fun PaletteHeading(palette: DynamicPalette?, dark: Boolean) {
    Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeading("System palette", if (dark) "Dark resources" else "Light resources")
        Label(
            if (palette == null)
                "System palettes require Android 12 or newer. No synthetic colors are shown."
            else
                "Every available system tone. Tap a color to copy its Android resource and ARGB value.",
            size = 12,
            color = LocalStudioColors.current.muted,
        )
        if (palette != null && palette.error == null)
            Label(
                "The error ramp requires Android 15 or newer.",
                size = 12,
                color = LocalStudioColors.current.muted,
            )
    }
}
