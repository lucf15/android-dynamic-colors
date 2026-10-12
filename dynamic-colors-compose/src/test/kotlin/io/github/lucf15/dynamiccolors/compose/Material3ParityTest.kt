/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.compose

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.test.core.app.ApplicationProvider
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.ColorRoles
import io.github.lucf15.dynamiccolors.dynamicColorsOrNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class Material3ParityTest {

    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Test
    @Config(sdk = [34])
    fun light_api34_matchesMaterial3_exceptErrorRoles() =
        assertParity(
            ours = dynamicColorsOrNull(context)!!.light,
            theirs = dynamicLightColorScheme(context),
            allowedDivergences =
                ERROR_ROLES - ColorRole.OnError, // onError happens to match in light
        )

    @Test
    @Config(sdk = [34])
    fun dark_api34_matchesMaterial3_exceptErrorRoles() =
        assertParity(
            ours = dynamicColorsOrNull(context)!!.dark,
            theirs = dynamicDarkColorScheme(context),
            allowedDivergences = ERROR_ROLES,
        )

    @Test
    @Config(sdk = [35])
    fun light_api35_matchesMaterial3_exceptErrorRoles() =
        assertParity(
            ours = dynamicColorsOrNull(context)!!.light,
            theirs = dynamicLightColorScheme(context),
            allowedDivergences = ERROR_ROLES - ColorRole.OnError,
        )

    @Test
    @Config(sdk = [31])
    fun light_api31_matchesMaterial3_exceptSnappedSurfaceRoles() =
        assertParity(
            ours = dynamicColorsOrNull(context)!!.light,
            theirs = dynamicLightColorScheme(context),
            allowedDivergences = SURFACE_SNAP_ROLES,
        )

    @Test
    @Config(sdk = [31])
    fun dark_api31_matchesMaterial3_exceptSnappedSurfaceRoles() =
        assertParity(
            ours = dynamicColorsOrNull(context)!!.dark,
            theirs = dynamicDarkColorScheme(context),
            allowedDivergences = SURFACE_SNAP_ROLES,
        )

    @Test
    @Config(sdk = [35])
    fun dark_api35() =
        assertParity(
            dynamicColorsOrNull(context)!!.dark,
            dynamicDarkColorScheme(context),
            ERROR_ROLES,
        )

    @Test
    @Config(sdk = [33])
    fun light_api33() =
        assertParity(
            dynamicColorsOrNull(context)!!.light,
            dynamicLightColorScheme(context),
            SURFACE_SNAP_ROLES,
        )

    @Test
    @Config(sdk = [33])
    fun dark_api33() =
        assertParity(
            dynamicColorsOrNull(context)!!.dark,
            dynamicDarkColorScheme(context),
            SURFACE_SNAP_ROLES,
        )

    /** Every divergence must be inside [allowedDivergences]; anything else fails the test. */
    private fun assertParity(
        ours: ColorRoles,
        theirs: ColorScheme,
        allowedDivergences: Set<ColorRole>,
    ) {
        val disagreements = mutableSetOf<ColorRole>()
        for (role in ColorRole.entries) {
            val m3 = theirs.roleColor(role).toArgb()
            if (ours[role] != m3) disagreements += role
        }
        assertTrue(
            "Unexpected mismatches: ${disagreements - allowedDivergences}",
            allowedDivergences.containsAll(disagreements),
        )
    }

    private companion object {
        val ERROR_ROLES: Set<ColorRole> =
            setOf(
                ColorRole.Error,
                ColorRole.OnError,
                ColorRole.ErrorContainer,
                ColorRole.OnErrorContainer,
            )

        // Roles Material 3 pins to a synthesised neutral tone on its API 31 path; we snap them.
        val SURFACE_SNAP_ROLES: Set<ColorRole> =
            setOf(
                ColorRole.Background,
                ColorRole.Surface,
                ColorRole.SurfaceBright,
                ColorRole.SurfaceDim,
                ColorRole.SurfaceContainer,
                ColorRole.SurfaceContainerHigh,
                ColorRole.SurfaceContainerHighest,
                ColorRole.SurfaceContainerLow,
                ColorRole.SurfaceContainerLowest,
            )
    }

    private fun ColorScheme.roleColor(role: ColorRole): Color =
        when (role) {
            ColorRole.Primary -> primary
            ColorRole.OnPrimary -> onPrimary
            ColorRole.PrimaryContainer -> primaryContainer
            ColorRole.OnPrimaryContainer -> onPrimaryContainer
            ColorRole.Secondary -> secondary
            ColorRole.OnSecondary -> onSecondary
            ColorRole.SecondaryContainer -> secondaryContainer
            ColorRole.OnSecondaryContainer -> onSecondaryContainer
            ColorRole.Tertiary -> tertiary
            ColorRole.OnTertiary -> onTertiary
            ColorRole.TertiaryContainer -> tertiaryContainer
            ColorRole.OnTertiaryContainer -> onTertiaryContainer
            ColorRole.Error -> error
            ColorRole.OnError -> onError
            ColorRole.ErrorContainer -> errorContainer
            ColorRole.OnErrorContainer -> onErrorContainer
            ColorRole.Background -> background
            ColorRole.OnBackground -> onBackground
            ColorRole.Surface -> surface
            ColorRole.OnSurface -> onSurface
            ColorRole.SurfaceVariant -> surfaceVariant
            ColorRole.OnSurfaceVariant -> onSurfaceVariant
            ColorRole.SurfaceBright -> surfaceBright
            ColorRole.SurfaceDim -> surfaceDim
            ColorRole.SurfaceContainerLowest -> surfaceContainerLowest
            ColorRole.SurfaceContainerLow -> surfaceContainerLow
            ColorRole.SurfaceContainer -> surfaceContainer
            ColorRole.SurfaceContainerHigh -> surfaceContainerHigh
            ColorRole.SurfaceContainerHighest -> surfaceContainerHighest
            ColorRole.Outline -> outline
            ColorRole.OutlineVariant -> outlineVariant
            ColorRole.PrimaryFixed -> primaryFixed
            ColorRole.PrimaryFixedDim -> primaryFixedDim
            ColorRole.OnPrimaryFixed -> onPrimaryFixed
            ColorRole.OnPrimaryFixedVariant -> onPrimaryFixedVariant
            ColorRole.SecondaryFixed -> secondaryFixed
            ColorRole.SecondaryFixedDim -> secondaryFixedDim
            ColorRole.OnSecondaryFixed -> onSecondaryFixed
            ColorRole.OnSecondaryFixedVariant -> onSecondaryFixedVariant
            ColorRole.TertiaryFixed -> tertiaryFixed
            ColorRole.TertiaryFixedDim -> tertiaryFixedDim
            ColorRole.OnTertiaryFixed -> onTertiaryFixed
            ColorRole.OnTertiaryFixedVariant -> onTertiaryFixedVariant
            ColorRole.Scrim -> scrim
            ColorRole.InversePrimary -> inversePrimary
            ColorRole.InverseSurface -> inverseSurface
            ColorRole.InverseOnSurface -> inverseOnSurface
            ColorRole.SurfaceTint -> surfaceTint
        }
}
