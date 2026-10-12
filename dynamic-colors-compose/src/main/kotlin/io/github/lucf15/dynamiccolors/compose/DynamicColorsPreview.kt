/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.lucf15.dynamiccolors.DynamicColorSamples
import io.github.lucf15.dynamiccolors.DynamicColors
import io.github.lucf15.dynamiccolors.DynamicPalette

private val sampleColors = DynamicColorSamples.colors()
private val samplePalette = DynamicColorSamples.palette()
internal val LocalPreviewColors = staticCompositionLocalOf<DynamicColors?> { sampleColors }
internal val LocalPreviewPalette = staticCompositionLocalOf<DynamicPalette?> { samplePalette }

/**
 * Supplies deterministic colors during inspection only. Pass null for both values to preview an
 * unsupported device. Outside inspection, the remembered readers continue to use system resources.
 */
@Composable
public fun DynamicColorsPreview(
    colors: DynamicColors? = sampleColors,
    palette: DynamicPalette? = samplePalette,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPreviewColors provides colors,
        LocalPreviewPalette provides palette,
        content = content,
    )
}
