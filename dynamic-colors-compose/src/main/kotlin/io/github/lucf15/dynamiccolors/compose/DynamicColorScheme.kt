/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.compose

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.ColorRoles

/** Immutable Compose-native semantic colors. Custom colors retain their ColorSpace. */
@Immutable
public class DynamicColorScheme(color: (ColorRole) -> Color) {
    private val colors = Array(ColorRole.entries.size) { color(ColorRole.entries[it]) }

    public operator fun get(role: ColorRole): Color = colors[role.ordinal]

    public val primary: Color
        get() = get(ColorRole.Primary)

    public val onPrimary: Color
        get() = get(ColorRole.OnPrimary)

    public val primaryContainer: Color
        get() = get(ColorRole.PrimaryContainer)

    public val onPrimaryContainer: Color
        get() = get(ColorRole.OnPrimaryContainer)

    public val secondary: Color
        get() = get(ColorRole.Secondary)

    public val onSecondary: Color
        get() = get(ColorRole.OnSecondary)

    public val secondaryContainer: Color
        get() = get(ColorRole.SecondaryContainer)

    public val onSecondaryContainer: Color
        get() = get(ColorRole.OnSecondaryContainer)

    public val tertiary: Color
        get() = get(ColorRole.Tertiary)

    public val onTertiary: Color
        get() = get(ColorRole.OnTertiary)

    public val tertiaryContainer: Color
        get() = get(ColorRole.TertiaryContainer)

    public val onTertiaryContainer: Color
        get() = get(ColorRole.OnTertiaryContainer)

    public val error: Color
        get() = get(ColorRole.Error)

    public val onError: Color
        get() = get(ColorRole.OnError)

    public val errorContainer: Color
        get() = get(ColorRole.ErrorContainer)

    public val onErrorContainer: Color
        get() = get(ColorRole.OnErrorContainer)

    public val background: Color
        get() = get(ColorRole.Background)

    public val onBackground: Color
        get() = get(ColorRole.OnBackground)

    public val surface: Color
        get() = get(ColorRole.Surface)

    public val onSurface: Color
        get() = get(ColorRole.OnSurface)

    public val surfaceVariant: Color
        get() = get(ColorRole.SurfaceVariant)

    public val onSurfaceVariant: Color
        get() = get(ColorRole.OnSurfaceVariant)

    public val surfaceBright: Color
        get() = get(ColorRole.SurfaceBright)

    public val surfaceDim: Color
        get() = get(ColorRole.SurfaceDim)

    public val surfaceContainerLowest: Color
        get() = get(ColorRole.SurfaceContainerLowest)

    public val surfaceContainerLow: Color
        get() = get(ColorRole.SurfaceContainerLow)

    public val surfaceContainer: Color
        get() = get(ColorRole.SurfaceContainer)

    public val surfaceContainerHigh: Color
        get() = get(ColorRole.SurfaceContainerHigh)

    public val surfaceContainerHighest: Color
        get() = get(ColorRole.SurfaceContainerHighest)

    public val outline: Color
        get() = get(ColorRole.Outline)

    public val outlineVariant: Color
        get() = get(ColorRole.OutlineVariant)

    public val primaryFixed: Color
        get() = get(ColorRole.PrimaryFixed)

    public val primaryFixedDim: Color
        get() = get(ColorRole.PrimaryFixedDim)

    public val onPrimaryFixed: Color
        get() = get(ColorRole.OnPrimaryFixed)

    public val onPrimaryFixedVariant: Color
        get() = get(ColorRole.OnPrimaryFixedVariant)

    public val secondaryFixed: Color
        get() = get(ColorRole.SecondaryFixed)

    public val secondaryFixedDim: Color
        get() = get(ColorRole.SecondaryFixedDim)

    public val onSecondaryFixed: Color
        get() = get(ColorRole.OnSecondaryFixed)

    public val onSecondaryFixedVariant: Color
        get() = get(ColorRole.OnSecondaryFixedVariant)

    public val tertiaryFixed: Color
        get() = get(ColorRole.TertiaryFixed)

    public val tertiaryFixedDim: Color
        get() = get(ColorRole.TertiaryFixedDim)

    public val onTertiaryFixed: Color
        get() = get(ColorRole.OnTertiaryFixed)

    public val onTertiaryFixedVariant: Color
        get() = get(ColorRole.OnTertiaryFixedVariant)

    public val scrim: Color
        get() = get(ColorRole.Scrim)

    public val inversePrimary: Color
        get() = get(ColorRole.InversePrimary)

    public val inverseSurface: Color
        get() = get(ColorRole.InverseSurface)

    public val inverseOnSurface: Color
        get() = get(ColorRole.InverseOnSurface)

    public val surfaceTint: Color
        get() = get(ColorRole.SurfaceTint)

    override fun equals(other: Any?): Boolean =
        this === other || (other is DynamicColorScheme && colors.contentEquals(other.colors))

    override fun hashCode(): Int = colors.contentHashCode()
}

/**
 * Converts a core snapshot into Compose colors; prefer remembering this conversion in composition.
 */
public fun ColorRoles.toComposeColorScheme(): DynamicColorScheme = DynamicColorScheme {
    Color(this[it])
}
