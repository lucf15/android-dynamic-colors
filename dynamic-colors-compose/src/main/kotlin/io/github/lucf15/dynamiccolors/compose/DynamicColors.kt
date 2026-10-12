/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.compose

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.ColorRoles
import io.github.lucf15.dynamiccolors.DynamicColors
import io.github.lucf15.dynamiccolors.DynamicPalette
import io.github.lucf15.dynamiccolors.TonalRamp
import io.github.lucf15.dynamiccolors.Tone
import io.github.lucf15.dynamiccolors.dynamicColorsOrNull
import io.github.lucf15.dynamiccolors.dynamicPaletteOrNull

/**
 * A configuration-aware snapshot, or null below API 31. Re-reads on context/configuration changes,
 * including system color overlay updates. [DynamicColorsPreview] supplies fixtures in previews.
 */
@Composable
public fun rememberDynamicColorsOrNull(): DynamicColors? {
    if (LocalInspectionMode.current) return LocalPreviewColors.current
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    return remember(context, configuration) { dynamicColorsOrNull(context) }
}

/**
 * Raw ramps for the current configuration, or null below API 31. Null [darkTheme] follows the
 * context; an explicit mode reads a configuration-scoped context without changing the app's theme.
 * Preview fixtures remain explicit snapshots and are returned unchanged.
 */
@Composable
public fun rememberDynamicPaletteOrNull(darkTheme: Boolean? = null): DynamicPalette? {
    if (LocalInspectionMode.current) return LocalPreviewPalette.current
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val paletteContext =
        remember(context, configuration, darkTheme) {
            if (darkTheme == null) context
            else
                context.createConfigurationContext(
                    Configuration(configuration).apply {
                        uiMode =
                            (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
                                if (darkTheme) Configuration.UI_MODE_NIGHT_YES
                                else Configuration.UI_MODE_NIGHT_NO
                    },
                )
        }
    return remember(paletteContext, configuration) { dynamicPaletteOrNull(paletteContext) }
}

/** Converts one role without allocating a second color model. */
public fun ColorRoles.color(role: ColorRole): Color = Color(this[role])

/** Converts one real system tone without interpolation. */
public fun TonalRamp.color(tone: Tone): Color = Color(this[tone])
