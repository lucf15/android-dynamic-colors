/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.internal

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.ColorRoles
import io.github.lucf15.dynamiccolors.DynamicColors
import io.github.lucf15.dynamiccolors.DynamicPalette
import io.github.lucf15.dynamiccolors.TonalRamp

/**
 * All platform resource access is centralized here and gated at the resource's introduction API.
 */
internal object AndroidColors {
    @RequiresApi(31)
    fun palette(context: Context): DynamicPalette {
        val resources = context.resources
        fun ramp(ids: IntArray) = TonalRamp { tone -> resources.getColor(ids[tone.ordinal], null) }
        return DynamicPalette(
            accent1 = ramp(Api31.ACCENT1_IDS),
            accent2 = ramp(Api31.ACCENT2_IDS),
            accent3 = ramp(Api31.ACCENT3_IDS),
            neutral1 = ramp(Api31.NEUTRAL1_IDS),
            neutral2 = ramp(Api31.NEUTRAL2_IDS),
            error = if (Build.VERSION.SDK_INT >= 35) ramp(Api35.ERROR_IDS) else null,
        )
    }

    @RequiresApi(31)
    fun colors(context: Context): DynamicColors {
        val light = context.forNightMode(false)
        val dark = context.forNightMode(true)
        return if (Build.VERSION.SDK_INT >= 34) {
            DynamicColors(readRoles(light.resources, false), readRoles(dark.resources, true))
        } else {
            DynamicColors(deriveRoles(palette(light), false), deriveRoles(palette(dark), true))
        }
    }

    // Unsuffixed resources can be configuration-sensitive too. Never read both modes through
    // one Context, even when both lookups use the same public resource ID (e.g. primary_fixed).
    private fun Context.forNightMode(dark: Boolean): Context {
        val mode = if (dark) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == mode) return this
        val configuration =
            Configuration(resources.configuration).apply {
                uiMode = (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or mode
            }
        return createConfigurationContext(configuration)
    }

    @RequiresApi(34)
    private fun readRoles(resources: Resources, dark: Boolean): ColorRoles = ColorRoles { role ->
        when (role) {
            ColorRole.Scrim,
            ColorRole.InversePrimary,
            ColorRole.InverseSurface,
            ColorRole.InverseOnSurface,
            ColorRole.SurfaceTint -> {
                if (Build.VERSION.SDK_INT >= 37) {
                    resources.getColor(Api37Roles.id(role, dark), null)
                } else {
                    fallbackRole(resources, role, dark)
                }
            }
            else -> resources.getColor(Api34Roles.id(role, dark), null)
        }
    }

    @RequiresApi(34)
    private fun fallbackRole(resources: Resources, role: ColorRole, dark: Boolean): Int {
        val id =
            when (role) {
                ColorRole.Scrim -> return 0xFF000000.toInt()
                ColorRole.InversePrimary -> Api34Roles.id(ColorRole.Primary, !dark)
                ColorRole.InverseSurface -> Api34Roles.id(ColorRole.Surface, !dark)
                ColorRole.InverseOnSurface -> Api34Roles.id(ColorRole.OnSurface, !dark)
                ColorRole.SurfaceTint -> Api34Roles.id(ColorRole.Primary, dark)
                else -> error("No fallback required for $role")
            }
        return resources.getColor(id, null)
    }

    // Arrays follow Tone.entries: darkest to lightest.
    @RequiresApi(31)
    private object Api31 {
        val ACCENT1_IDS =
            intArrayOf(
                android.R.color.system_accent1_1000,
                android.R.color.system_accent1_900,
                android.R.color.system_accent1_800,
                android.R.color.system_accent1_700,
                android.R.color.system_accent1_600,
                android.R.color.system_accent1_500,
                android.R.color.system_accent1_400,
                android.R.color.system_accent1_300,
                android.R.color.system_accent1_200,
                android.R.color.system_accent1_100,
                android.R.color.system_accent1_50,
                android.R.color.system_accent1_10,
                android.R.color.system_accent1_0,
            )
        val ACCENT2_IDS =
            intArrayOf(
                android.R.color.system_accent2_1000,
                android.R.color.system_accent2_900,
                android.R.color.system_accent2_800,
                android.R.color.system_accent2_700,
                android.R.color.system_accent2_600,
                android.R.color.system_accent2_500,
                android.R.color.system_accent2_400,
                android.R.color.system_accent2_300,
                android.R.color.system_accent2_200,
                android.R.color.system_accent2_100,
                android.R.color.system_accent2_50,
                android.R.color.system_accent2_10,
                android.R.color.system_accent2_0,
            )
        val ACCENT3_IDS =
            intArrayOf(
                android.R.color.system_accent3_1000,
                android.R.color.system_accent3_900,
                android.R.color.system_accent3_800,
                android.R.color.system_accent3_700,
                android.R.color.system_accent3_600,
                android.R.color.system_accent3_500,
                android.R.color.system_accent3_400,
                android.R.color.system_accent3_300,
                android.R.color.system_accent3_200,
                android.R.color.system_accent3_100,
                android.R.color.system_accent3_50,
                android.R.color.system_accent3_10,
                android.R.color.system_accent3_0,
            )
        val NEUTRAL1_IDS =
            intArrayOf(
                android.R.color.system_neutral1_1000,
                android.R.color.system_neutral1_900,
                android.R.color.system_neutral1_800,
                android.R.color.system_neutral1_700,
                android.R.color.system_neutral1_600,
                android.R.color.system_neutral1_500,
                android.R.color.system_neutral1_400,
                android.R.color.system_neutral1_300,
                android.R.color.system_neutral1_200,
                android.R.color.system_neutral1_100,
                android.R.color.system_neutral1_50,
                android.R.color.system_neutral1_10,
                android.R.color.system_neutral1_0,
            )
        val NEUTRAL2_IDS =
            intArrayOf(
                android.R.color.system_neutral2_1000,
                android.R.color.system_neutral2_900,
                android.R.color.system_neutral2_800,
                android.R.color.system_neutral2_700,
                android.R.color.system_neutral2_600,
                android.R.color.system_neutral2_500,
                android.R.color.system_neutral2_400,
                android.R.color.system_neutral2_300,
                android.R.color.system_neutral2_200,
                android.R.color.system_neutral2_100,
                android.R.color.system_neutral2_50,
                android.R.color.system_neutral2_10,
                android.R.color.system_neutral2_0,
            )
    }

    @RequiresApi(35)
    private object Api35 {
        val ERROR_IDS =
            intArrayOf(
                android.R.color.system_error_1000,
                android.R.color.system_error_900,
                android.R.color.system_error_800,
                android.R.color.system_error_700,
                android.R.color.system_error_600,
                android.R.color.system_error_500,
                android.R.color.system_error_400,
                android.R.color.system_error_300,
                android.R.color.system_error_200,
                android.R.color.system_error_100,
                android.R.color.system_error_50,
                android.R.color.system_error_10,
                android.R.color.system_error_0,
            )
    }

    @RequiresApi(34)
    private object Api34Roles {
        @ColorRes
        fun id(role: ColorRole, dark: Boolean): Int = if (dark) darkId(role) else lightId(role)

        private fun lightId(role: ColorRole): Int =
            when (role) {
                ColorRole.Primary -> android.R.color.system_primary_light
                ColorRole.OnPrimary -> android.R.color.system_on_primary_light
                ColorRole.PrimaryContainer -> android.R.color.system_primary_container_light
                ColorRole.OnPrimaryContainer -> android.R.color.system_on_primary_container_light
                ColorRole.Secondary -> android.R.color.system_secondary_light
                ColorRole.OnSecondary -> android.R.color.system_on_secondary_light
                ColorRole.SecondaryContainer -> android.R.color.system_secondary_container_light
                ColorRole.OnSecondaryContainer ->
                    android.R.color.system_on_secondary_container_light
                ColorRole.Tertiary -> android.R.color.system_tertiary_light
                ColorRole.OnTertiary -> android.R.color.system_on_tertiary_light
                ColorRole.TertiaryContainer -> android.R.color.system_tertiary_container_light
                ColorRole.OnTertiaryContainer -> android.R.color.system_on_tertiary_container_light
                ColorRole.Error -> android.R.color.system_error_light
                ColorRole.OnError -> android.R.color.system_on_error_light
                ColorRole.ErrorContainer -> android.R.color.system_error_container_light
                ColorRole.OnErrorContainer -> android.R.color.system_on_error_container_light
                ColorRole.Background -> android.R.color.system_background_light
                ColorRole.OnBackground -> android.R.color.system_on_background_light
                ColorRole.Surface -> android.R.color.system_surface_light
                ColorRole.OnSurface -> android.R.color.system_on_surface_light
                ColorRole.SurfaceVariant -> android.R.color.system_surface_variant_light
                ColorRole.OnSurfaceVariant -> android.R.color.system_on_surface_variant_light
                ColorRole.SurfaceBright -> android.R.color.system_surface_bright_light
                ColorRole.SurfaceDim -> android.R.color.system_surface_dim_light
                ColorRole.SurfaceContainerLowest ->
                    android.R.color.system_surface_container_lowest_light
                ColorRole.SurfaceContainerLow -> android.R.color.system_surface_container_low_light
                ColorRole.SurfaceContainer -> android.R.color.system_surface_container_light
                ColorRole.SurfaceContainerHigh ->
                    android.R.color.system_surface_container_high_light
                ColorRole.SurfaceContainerHighest ->
                    android.R.color.system_surface_container_highest_light
                ColorRole.Outline -> android.R.color.system_outline_light
                ColorRole.OutlineVariant -> android.R.color.system_outline_variant_light
                ColorRole.PrimaryFixed -> android.R.color.system_primary_fixed
                ColorRole.PrimaryFixedDim -> android.R.color.system_primary_fixed_dim
                ColorRole.OnPrimaryFixed -> android.R.color.system_on_primary_fixed
                ColorRole.OnPrimaryFixedVariant -> android.R.color.system_on_primary_fixed_variant
                ColorRole.SecondaryFixed -> android.R.color.system_secondary_fixed
                ColorRole.SecondaryFixedDim -> android.R.color.system_secondary_fixed_dim
                ColorRole.OnSecondaryFixed -> android.R.color.system_on_secondary_fixed
                ColorRole.OnSecondaryFixedVariant ->
                    android.R.color.system_on_secondary_fixed_variant
                ColorRole.TertiaryFixed -> android.R.color.system_tertiary_fixed
                ColorRole.TertiaryFixedDim -> android.R.color.system_tertiary_fixed_dim
                ColorRole.OnTertiaryFixed -> android.R.color.system_on_tertiary_fixed
                ColorRole.OnTertiaryFixedVariant -> android.R.color.system_on_tertiary_fixed_variant
                ColorRole.Scrim,
                ColorRole.InversePrimary,
                ColorRole.InverseSurface,
                ColorRole.InverseOnSurface,
                ColorRole.SurfaceTint ->
                    throw IllegalArgumentException("$role has no API 34 resource")
            }

        private fun darkId(role: ColorRole): Int =
            when (role) {
                ColorRole.Primary -> android.R.color.system_primary_dark
                ColorRole.OnPrimary -> android.R.color.system_on_primary_dark
                ColorRole.PrimaryContainer -> android.R.color.system_primary_container_dark
                ColorRole.OnPrimaryContainer -> android.R.color.system_on_primary_container_dark
                ColorRole.Secondary -> android.R.color.system_secondary_dark
                ColorRole.OnSecondary -> android.R.color.system_on_secondary_dark
                ColorRole.SecondaryContainer -> android.R.color.system_secondary_container_dark
                ColorRole.OnSecondaryContainer -> android.R.color.system_on_secondary_container_dark
                ColorRole.Tertiary -> android.R.color.system_tertiary_dark
                ColorRole.OnTertiary -> android.R.color.system_on_tertiary_dark
                ColorRole.TertiaryContainer -> android.R.color.system_tertiary_container_dark
                ColorRole.OnTertiaryContainer -> android.R.color.system_on_tertiary_container_dark
                ColorRole.Error -> android.R.color.system_error_dark
                ColorRole.OnError -> android.R.color.system_on_error_dark
                ColorRole.ErrorContainer -> android.R.color.system_error_container_dark
                ColorRole.OnErrorContainer -> android.R.color.system_on_error_container_dark
                ColorRole.Background -> android.R.color.system_background_dark
                ColorRole.OnBackground -> android.R.color.system_on_background_dark
                ColorRole.Surface -> android.R.color.system_surface_dark
                ColorRole.OnSurface -> android.R.color.system_on_surface_dark
                ColorRole.SurfaceVariant -> android.R.color.system_surface_variant_dark
                ColorRole.OnSurfaceVariant -> android.R.color.system_on_surface_variant_dark
                ColorRole.SurfaceBright -> android.R.color.system_surface_bright_dark
                ColorRole.SurfaceDim -> android.R.color.system_surface_dim_dark
                ColorRole.SurfaceContainerLowest ->
                    android.R.color.system_surface_container_lowest_dark
                ColorRole.SurfaceContainerLow -> android.R.color.system_surface_container_low_dark
                ColorRole.SurfaceContainer -> android.R.color.system_surface_container_dark
                ColorRole.SurfaceContainerHigh -> android.R.color.system_surface_container_high_dark
                ColorRole.SurfaceContainerHighest ->
                    android.R.color.system_surface_container_highest_dark
                ColorRole.Outline -> android.R.color.system_outline_dark
                ColorRole.OutlineVariant -> android.R.color.system_outline_variant_dark
                // Same resource ID; its value can depend on this set's day/night configuration.
                ColorRole.PrimaryFixed -> android.R.color.system_primary_fixed
                ColorRole.PrimaryFixedDim -> android.R.color.system_primary_fixed_dim
                ColorRole.OnPrimaryFixed -> android.R.color.system_on_primary_fixed
                ColorRole.OnPrimaryFixedVariant -> android.R.color.system_on_primary_fixed_variant
                ColorRole.SecondaryFixed -> android.R.color.system_secondary_fixed
                ColorRole.SecondaryFixedDim -> android.R.color.system_secondary_fixed_dim
                ColorRole.OnSecondaryFixed -> android.R.color.system_on_secondary_fixed
                ColorRole.OnSecondaryFixedVariant ->
                    android.R.color.system_on_secondary_fixed_variant
                ColorRole.TertiaryFixed -> android.R.color.system_tertiary_fixed
                ColorRole.TertiaryFixedDim -> android.R.color.system_tertiary_fixed_dim
                ColorRole.OnTertiaryFixed -> android.R.color.system_on_tertiary_fixed
                ColorRole.OnTertiaryFixedVariant -> android.R.color.system_on_tertiary_fixed_variant
                ColorRole.Scrim,
                ColorRole.InversePrimary,
                ColorRole.InverseSurface,
                ColorRole.InverseOnSurface,
                ColorRole.SurfaceTint ->
                    throw IllegalArgumentException("$role has no API 34 resource")
            }
    }

    @RequiresApi(37)
    private object Api37Roles {
        @ColorRes
        fun id(role: ColorRole, dark: Boolean): Int =
            when (role) {
                ColorRole.Scrim ->
                    if (dark) android.R.color.system_scrim_dark
                    else android.R.color.system_scrim_light
                ColorRole.InversePrimary ->
                    if (dark) android.R.color.system_inverse_primary_dark
                    else android.R.color.system_inverse_primary_light
                ColorRole.InverseSurface ->
                    if (dark) android.R.color.system_inverse_surface_dark
                    else android.R.color.system_inverse_surface_light
                ColorRole.InverseOnSurface ->
                    if (dark) android.R.color.system_inverse_on_surface_dark
                    else android.R.color.system_inverse_on_surface_light
                ColorRole.SurfaceTint ->
                    if (dark) android.R.color.system_surface_tint_dark
                    else android.R.color.system_surface_tint_light
                else -> throw IllegalArgumentException("$role is not an API 37 role")
            }
    }
}
