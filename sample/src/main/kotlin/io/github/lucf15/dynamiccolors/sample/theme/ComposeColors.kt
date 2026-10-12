/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.compose.DynamicColorScheme

/** Compose colors stay in Compose's native type, including throughout animation. */
@Immutable
internal data class StudioColors(
    val background: Color,
    val surface: Color,
    val text: Color,
    val muted: Color,
    val accent: Color,
    val onAccent: Color,
    val outline: Color,
    val accentSurface: Color,
    val onAccentSurface: Color,
    val secondary: Color,
    val error: Color,
    val errorSurface: Color,
    val onErrorSurface: Color,
)

internal fun DynamicColorScheme.toStudioColors() =
    StudioColors(
        background = background,
        surface = surfaceContainerLow,
        text = onSurface,
        muted = onSurfaceVariant,
        accent = primary,
        onAccent = onPrimary,
        outline = outlineVariant,
        accentSurface = primaryContainer,
        onAccentSurface = onPrimaryContainer,
        secondary = tertiary,
        error = error,
        errorSurface = errorContainer,
        onErrorSurface = onErrorContainer,
    )

/** Convert once at the AndroidView boundary. */
internal fun StudioColors.toAppColors() =
    AppColors(
        background = background.toArgb(),
        surface = surface.toArgb(),
        text = text.toArgb(),
        muted = muted.toArgb(),
        accent = accent.toArgb(),
        onAccent = onAccent.toArgb(),
        outline = outline.toArgb(),
        accentSurface = accentSurface.toArgb(),
        onAccentSurface = onAccentSurface.toArgb(),
        secondary = secondary.toArgb(),
        error = error.toArgb(),
        errorSurface = errorSurface.toArgb(),
        onErrorSurface = onErrorSurface.toArgb(),
    )

internal val LightScheme = LightDefaults.toColorScheme()
internal val DarkScheme = DarkDefaults.toColorScheme()

/** Complete role mapping for the sample's own quiet palette. */
private fun AppColors.toColorScheme() = DynamicColorScheme { role ->
    Color(
        when (role) {
            ColorRole.Primary,
            ColorRole.SurfaceTint,
            ColorRole.InversePrimary -> accent
            ColorRole.OnPrimary -> onAccent
            ColorRole.PrimaryContainer,
            ColorRole.PrimaryFixed,
            ColorRole.PrimaryFixedDim -> accentSurface
            ColorRole.OnPrimaryContainer,
            ColorRole.OnPrimaryFixed,
            ColorRole.OnPrimaryFixedVariant -> onAccentSurface
            ColorRole.Secondary,
            ColorRole.Tertiary -> secondary
            ColorRole.OnSecondary,
            ColorRole.OnTertiary -> onAccent
            ColorRole.SecondaryContainer,
            ColorRole.SecondaryFixed,
            ColorRole.SecondaryFixedDim,
            ColorRole.TertiaryContainer,
            ColorRole.TertiaryFixed,
            ColorRole.TertiaryFixedDim -> accentSurface
            ColorRole.OnSecondaryContainer,
            ColorRole.OnSecondaryFixed,
            ColorRole.OnSecondaryFixedVariant,
            ColorRole.OnTertiaryContainer,
            ColorRole.OnTertiaryFixed,
            ColorRole.OnTertiaryFixedVariant -> onAccentSurface
            ColorRole.Error -> error
            ColorRole.OnError -> onAccent
            ColorRole.ErrorContainer -> errorSurface
            ColorRole.OnErrorContainer -> onErrorSurface
            ColorRole.Background,
            ColorRole.SurfaceDim,
            ColorRole.SurfaceContainerLowest -> background
            ColorRole.Surface,
            ColorRole.SurfaceBright,
            ColorRole.SurfaceContainerLow,
            ColorRole.SurfaceContainer,
            ColorRole.SurfaceContainerHigh,
            ColorRole.SurfaceContainerHighest,
            ColorRole.SurfaceVariant -> surface
            ColorRole.OnBackground,
            ColorRole.OnSurface -> text
            ColorRole.OnSurfaceVariant -> muted
            ColorRole.Outline,
            ColorRole.OutlineVariant -> outline
            ColorRole.InverseSurface -> text
            ColorRole.InverseOnSurface -> background
            ColorRole.Scrim -> android.graphics.Color.BLACK
        },
    )
}
