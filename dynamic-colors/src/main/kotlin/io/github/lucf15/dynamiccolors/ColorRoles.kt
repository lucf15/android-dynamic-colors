/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

import androidx.annotation.ColorInt

/**
 * A complete, immutable set of semantic colors for one mode. Every accessor returns a color. The
 * factory callback runs exactly once per role; partial sets are not representable.
 */
public class ColorRoles(color: (ColorRole) -> Int) {
    private val colors: IntArray = IntArray(ColorRole.entries.size) { color(ColorRole.entries[it]) }

    @ColorInt public operator fun get(role: ColorRole): Int = colors[role.ordinal]

    @get:ColorInt
    public val primary: Int
        get() = get(ColorRole.Primary)

    @get:ColorInt
    public val onPrimary: Int
        get() = get(ColorRole.OnPrimary)

    @get:ColorInt
    public val primaryContainer: Int
        get() = get(ColorRole.PrimaryContainer)

    @get:ColorInt
    public val onPrimaryContainer: Int
        get() = get(ColorRole.OnPrimaryContainer)

    @get:ColorInt
    public val secondary: Int
        get() = get(ColorRole.Secondary)

    @get:ColorInt
    public val onSecondary: Int
        get() = get(ColorRole.OnSecondary)

    @get:ColorInt
    public val secondaryContainer: Int
        get() = get(ColorRole.SecondaryContainer)

    @get:ColorInt
    public val onSecondaryContainer: Int
        get() = get(ColorRole.OnSecondaryContainer)

    @get:ColorInt
    public val tertiary: Int
        get() = get(ColorRole.Tertiary)

    @get:ColorInt
    public val onTertiary: Int
        get() = get(ColorRole.OnTertiary)

    @get:ColorInt
    public val tertiaryContainer: Int
        get() = get(ColorRole.TertiaryContainer)

    @get:ColorInt
    public val onTertiaryContainer: Int
        get() = get(ColorRole.OnTertiaryContainer)

    @get:ColorInt
    public val error: Int
        get() = get(ColorRole.Error)

    @get:ColorInt
    public val onError: Int
        get() = get(ColorRole.OnError)

    @get:ColorInt
    public val errorContainer: Int
        get() = get(ColorRole.ErrorContainer)

    @get:ColorInt
    public val onErrorContainer: Int
        get() = get(ColorRole.OnErrorContainer)

    @get:ColorInt
    public val background: Int
        get() = get(ColorRole.Background)

    @get:ColorInt
    public val onBackground: Int
        get() = get(ColorRole.OnBackground)

    @get:ColorInt
    public val surface: Int
        get() = get(ColorRole.Surface)

    @get:ColorInt
    public val onSurface: Int
        get() = get(ColorRole.OnSurface)

    @get:ColorInt
    public val surfaceVariant: Int
        get() = get(ColorRole.SurfaceVariant)

    @get:ColorInt
    public val onSurfaceVariant: Int
        get() = get(ColorRole.OnSurfaceVariant)

    @get:ColorInt
    public val surfaceBright: Int
        get() = get(ColorRole.SurfaceBright)

    @get:ColorInt
    public val surfaceDim: Int
        get() = get(ColorRole.SurfaceDim)

    @get:ColorInt
    public val surfaceContainerLowest: Int
        get() = get(ColorRole.SurfaceContainerLowest)

    @get:ColorInt
    public val surfaceContainerLow: Int
        get() = get(ColorRole.SurfaceContainerLow)

    @get:ColorInt
    public val surfaceContainer: Int
        get() = get(ColorRole.SurfaceContainer)

    @get:ColorInt
    public val surfaceContainerHigh: Int
        get() = get(ColorRole.SurfaceContainerHigh)

    @get:ColorInt
    public val surfaceContainerHighest: Int
        get() = get(ColorRole.SurfaceContainerHighest)

    @get:ColorInt
    public val outline: Int
        get() = get(ColorRole.Outline)

    @get:ColorInt
    public val outlineVariant: Int
        get() = get(ColorRole.OutlineVariant)

    @get:ColorInt
    public val primaryFixed: Int
        get() = get(ColorRole.PrimaryFixed)

    @get:ColorInt
    public val primaryFixedDim: Int
        get() = get(ColorRole.PrimaryFixedDim)

    @get:ColorInt
    public val onPrimaryFixed: Int
        get() = get(ColorRole.OnPrimaryFixed)

    @get:ColorInt
    public val onPrimaryFixedVariant: Int
        get() = get(ColorRole.OnPrimaryFixedVariant)

    @get:ColorInt
    public val secondaryFixed: Int
        get() = get(ColorRole.SecondaryFixed)

    @get:ColorInt
    public val secondaryFixedDim: Int
        get() = get(ColorRole.SecondaryFixedDim)

    @get:ColorInt
    public val onSecondaryFixed: Int
        get() = get(ColorRole.OnSecondaryFixed)

    @get:ColorInt
    public val onSecondaryFixedVariant: Int
        get() = get(ColorRole.OnSecondaryFixedVariant)

    @get:ColorInt
    public val tertiaryFixed: Int
        get() = get(ColorRole.TertiaryFixed)

    @get:ColorInt
    public val tertiaryFixedDim: Int
        get() = get(ColorRole.TertiaryFixedDim)

    @get:ColorInt
    public val onTertiaryFixed: Int
        get() = get(ColorRole.OnTertiaryFixed)

    @get:ColorInt
    public val onTertiaryFixedVariant: Int
        get() = get(ColorRole.OnTertiaryFixedVariant)

    @get:ColorInt
    public val scrim: Int
        get() = get(ColorRole.Scrim)

    @get:ColorInt
    public val inversePrimary: Int
        get() = get(ColorRole.InversePrimary)

    @get:ColorInt
    public val inverseSurface: Int
        get() = get(ColorRole.InverseSurface)

    @get:ColorInt
    public val inverseOnSurface: Int
        get() = get(ColorRole.InverseOnSurface)

    @get:ColorInt
    public val surfaceTint: Int
        get() = get(ColorRole.SurfaceTint)

    override fun equals(other: Any?): Boolean =
        this === other || (other is ColorRoles && colors.contentEquals(other.colors))

    override fun hashCode(): Int = colors.contentHashCode()

    override fun toString(): String =
        "ColorRoles(${ColorRole.entries.joinToString { "$it=#${get(it).toUInt().toString(16).padStart(8, '0')}" }})"
}
