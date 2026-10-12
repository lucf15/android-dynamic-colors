/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.internal

import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.ColorRoles
import io.github.lucf15.dynamiccolors.DynamicPalette
import io.github.lucf15.dynamiccolors.Tone

/**
 * API 31–33 compatibility mapping. Only real tone stops are used; see docs/platform-behavior.md.
 */
internal fun deriveRoles(palette: DynamicPalette, dark: Boolean): ColorRoles = ColorRoles { role ->
    if (dark) darkColor(palette, role) else lightColor(palette, role)
}

private fun lightColor(palette: DynamicPalette, role: ColorRole): Int =
    when (role) {
        ColorRole.Primary -> palette.accent1[Tone.T40]
        ColorRole.OnPrimary -> palette.accent1[Tone.T100]
        ColorRole.PrimaryContainer -> palette.accent1[Tone.T90]
        ColorRole.OnPrimaryContainer -> palette.accent1[Tone.T10]
        ColorRole.InversePrimary -> palette.accent1[Tone.T80]
        ColorRole.Secondary -> palette.accent2[Tone.T40]
        ColorRole.OnSecondary -> palette.accent2[Tone.T100]
        ColorRole.SecondaryContainer -> palette.accent2[Tone.T90]
        ColorRole.OnSecondaryContainer -> palette.accent2[Tone.T10]
        ColorRole.Tertiary -> palette.accent3[Tone.T40]
        ColorRole.OnTertiary -> palette.accent3[Tone.T100]
        ColorRole.TertiaryContainer -> palette.accent3[Tone.T90]
        ColorRole.OnTertiaryContainer -> palette.accent3[Tone.T10]
        ColorRole.Background -> palette.neutral2[Tone.T99] // M3: neutralVariant98
        ColorRole.OnBackground -> palette.neutral2[Tone.T10]
        ColorRole.Surface -> palette.neutral2[Tone.T99] // M3: neutralVariant98
        ColorRole.OnSurface -> palette.neutral2[Tone.T10]
        ColorRole.SurfaceVariant -> palette.neutral2[Tone.T90]
        ColorRole.OnSurfaceVariant -> palette.neutral2[Tone.T30]
        ColorRole.InverseSurface -> palette.neutral2[Tone.T20]
        ColorRole.InverseOnSurface -> palette.neutral2[Tone.T95]
        ColorRole.Outline -> palette.neutral2[Tone.T50]
        ColorRole.OutlineVariant -> palette.neutral2[Tone.T80]
        ColorRole.Scrim -> palette.neutral2[Tone.T0]
        ColorRole.SurfaceBright -> palette.neutral2[Tone.T99] // M3: neutralVariant98
        ColorRole.SurfaceDim -> palette.neutral2[Tone.T90] // M3: neutralVariant87
        ColorRole.SurfaceContainerLowest -> palette.neutral2[Tone.T100]
        ColorRole.SurfaceContainerLow -> palette.neutral2[Tone.T95] // M3: neutralVariant96
        ColorRole.SurfaceContainer -> palette.neutral2[Tone.T95] // M3: neutralVariant94
        ColorRole.SurfaceContainerHigh -> palette.neutral2[Tone.T90] // M3: neutralVariant92
        ColorRole.SurfaceContainerHighest -> palette.neutral2[Tone.T90]
        ColorRole.SurfaceTint -> palette.accent1[Tone.T40]
        ColorRole.PrimaryFixed -> palette.accent1[Tone.T90]
        ColorRole.PrimaryFixedDim -> palette.accent1[Tone.T80]
        ColorRole.OnPrimaryFixed -> palette.accent1[Tone.T10]
        ColorRole.OnPrimaryFixedVariant -> palette.accent1[Tone.T30]
        ColorRole.SecondaryFixed -> palette.accent2[Tone.T90]
        ColorRole.SecondaryFixedDim -> palette.accent2[Tone.T80]
        ColorRole.OnSecondaryFixed -> palette.accent2[Tone.T10]
        ColorRole.OnSecondaryFixedVariant -> palette.accent2[Tone.T30]
        ColorRole.TertiaryFixed -> palette.accent3[Tone.T90]
        ColorRole.TertiaryFixedDim -> palette.accent3[Tone.T80]
        ColorRole.OnTertiaryFixed -> palette.accent3[Tone.T10]
        ColorRole.OnTertiaryFixedVariant -> palette.accent3[Tone.T30]
        ColorRole.Error -> 0xFFB3261E.toInt()
        ColorRole.OnError -> 0xFFFFFFFF.toInt()
        ColorRole.ErrorContainer -> 0xFFF9DEDC.toInt()
        ColorRole.OnErrorContainer -> 0xFF410E0B.toInt()
    }

private fun darkColor(palette: DynamicPalette, role: ColorRole): Int =
    when (role) {
        ColorRole.Primary -> palette.accent1[Tone.T80]
        ColorRole.OnPrimary -> palette.accent1[Tone.T20]
        ColorRole.PrimaryContainer -> palette.accent1[Tone.T30]
        ColorRole.OnPrimaryContainer -> palette.accent1[Tone.T90]
        ColorRole.InversePrimary -> palette.accent1[Tone.T40]
        ColorRole.Secondary -> palette.accent2[Tone.T80]
        ColorRole.OnSecondary -> palette.accent2[Tone.T20]
        ColorRole.SecondaryContainer -> palette.accent2[Tone.T30]
        ColorRole.OnSecondaryContainer -> palette.accent2[Tone.T90]
        ColorRole.Tertiary -> palette.accent3[Tone.T80]
        ColorRole.OnTertiary -> palette.accent3[Tone.T20]
        ColorRole.TertiaryContainer -> palette.accent3[Tone.T30]
        ColorRole.OnTertiaryContainer -> palette.accent3[Tone.T90]
        ColorRole.Background -> palette.neutral2[Tone.T10] // M3: neutralVariant6
        ColorRole.OnBackground -> palette.neutral2[Tone.T90]
        ColorRole.Surface -> palette.neutral2[Tone.T10] // M3: neutralVariant6
        ColorRole.OnSurface -> palette.neutral2[Tone.T90]
        ColorRole.SurfaceVariant -> palette.neutral2[Tone.T30]
        ColorRole.OnSurfaceVariant -> palette.neutral2[Tone.T80]
        ColorRole.InverseSurface -> palette.neutral2[Tone.T90]
        ColorRole.InverseOnSurface -> palette.neutral2[Tone.T20]
        ColorRole.Outline -> palette.neutral2[Tone.T60]
        ColorRole.OutlineVariant -> palette.neutral2[Tone.T30]
        ColorRole.Scrim -> palette.neutral2[Tone.T0]
        ColorRole.SurfaceBright -> palette.neutral2[Tone.T20] // M3: neutralVariant24
        ColorRole.SurfaceDim -> palette.neutral2[Tone.T10] // M3: neutralVariant6
        ColorRole.SurfaceContainerLowest -> palette.neutral2[Tone.T0] // M3: neutralVariant4
        ColorRole.SurfaceContainerLow -> palette.neutral2[Tone.T10]
        ColorRole.SurfaceContainer -> palette.neutral2[Tone.T10] // M3: neutralVariant12
        ColorRole.SurfaceContainerHigh -> palette.neutral2[Tone.T20] // M3: neutralVariant17
        ColorRole.SurfaceContainerHighest -> palette.neutral2[Tone.T20] // M3: neutralVariant22
        ColorRole.SurfaceTint -> palette.accent1[Tone.T80]
        ColorRole.PrimaryFixed -> palette.accent1[Tone.T90]
        ColorRole.PrimaryFixedDim -> palette.accent1[Tone.T80]
        ColorRole.OnPrimaryFixed -> palette.accent1[Tone.T10]
        ColorRole.OnPrimaryFixedVariant -> palette.accent1[Tone.T30]
        ColorRole.SecondaryFixed -> palette.accent2[Tone.T90]
        ColorRole.SecondaryFixedDim -> palette.accent2[Tone.T80]
        ColorRole.OnSecondaryFixed -> palette.accent2[Tone.T10]
        ColorRole.OnSecondaryFixedVariant -> palette.accent2[Tone.T30]
        ColorRole.TertiaryFixed -> palette.accent3[Tone.T90]
        ColorRole.TertiaryFixedDim -> palette.accent3[Tone.T80]
        ColorRole.OnTertiaryFixed -> palette.accent3[Tone.T10]
        ColorRole.OnTertiaryFixedVariant -> palette.accent3[Tone.T30]
        ColorRole.Error -> 0xFFF2B8B5.toInt()
        ColorRole.OnError -> 0xFF601410.toInt()
        ColorRole.ErrorContainer -> 0xFF8C1D18.toInt()
        ColorRole.OnErrorContainer -> 0xFFF9DEDC.toInt()
    }
