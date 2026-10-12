/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
class AndroidColorsTest {
    private val context: Context
        get() = ApplicationProvider.getApplicationContext()

    @Test
    @Config(sdk = [21, 23, 30])
    fun unavailablePlatformsReturnNull() {
        assertFalse(isDynamicColorAvailable())
        assertNull(dynamicColorsOrNull(context))
        assertNull(dynamicPaletteOrNull(context))
    }

    @Test
    @Config(sdk = [31, 33, 34, 35])
    fun everyTonalStopMatchesItsResource() {
        val palette = requireNotNull(dynamicPaletteOrNull(context))
        val names = listOf("accent1", "accent2", "accent3", "neutral1", "neutral2", "error")
        for (family in PaletteFamily.entries) {
            val ramp = palette[family]
            if (family == PaletteFamily.Error && Build.VERSION.SDK_INT < 35) {
                assertNull(ramp)
            } else {
                requireNotNull(ramp)
                for (tone in Tone.entries) {
                    val name = "system_${names[family.ordinal]}_${tone.resourceStep}"
                    val id = context.resources.getIdentifier(name, "color", "android")
                    assertNotEquals(name, 0, id)
                    assertEquals(name, context.getColor(id), ramp[tone])
                }
            }
        }
    }

    @Test
    @Config(sdk = [34, 35])
    fun allDirectRolesMatchThePublicPlatformResources() {
        val colors = requireNotNull(dynamicColorsOrNull(context))
        val fallbackRoles =
            setOf(
                ColorRole.Scrim,
                ColorRole.InversePrimary,
                ColorRole.InverseSurface,
                ColorRole.InverseOnSurface,
                ColorRole.SurfaceTint,
            )
        for (dark in listOf(false, true)) {
            val mode = if (dark) "dark" else "light"
            val set = if (dark) colors.dark else colors.light
            for (role in ColorRole.entries - fallbackRoles) {
                val snake = role.name.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
                val name = "system_" + snake + if ("Fixed" in role.name) "" else "_$mode"
                val id = context.resources.getIdentifier(name, "color", "android")
                assertNotEquals(name, 0, id)
                assertEquals(name, context.getColor(id), set[role])
            }
            assertEquals(0xFF000000.toInt(), set.scrim)
            assertEquals(set.primary, set.surfaceTint)
        }
    }

    @Test
    @Config(sdk = [31, 33])
    fun compatibilityMappingUsesRealStopsAndExplicitErrorDefaults() {
        val palette = requireNotNull(dynamicPaletteOrNull(context))
        val colors = requireNotNull(dynamicColorsOrNull(context))
        assertEquals(palette.accent1[Tone.T40], colors.light.primary)
        assertEquals(palette.accent1[Tone.T80], colors.dark.primary)
        assertEquals(palette.neutral2[Tone.T20], colors.dark.surfaceBright)
        assertEquals(palette.neutral2[Tone.T0], colors.dark.surfaceContainerLowest)
        assertEquals(0xFFB3261E.toInt(), colors.light.error)
    }

    @Test
    fun bothModesResolveTheirOwnConfigurationEvenForTheSameResourceId() {
        // Force different day/night values for every resource, including unsuffixed fixed roles.
        // AOSP 37 has these qualifiers; older Robolectric SDKs don't ship those resource tables.
        val original = Build.VERSION.SDK_INT
        ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", 37)
        try {
            for (startsDark in listOf(false, true)) {
                val source = ModeContext(context, startsDark)
                val colors = requireNotNull(dynamicColorsOrNull(source))
                assertEquals(
                    ModeContext.value(android.R.color.system_primary_fixed, false),
                    colors.light.primaryFixed,
                )
                assertEquals(
                    ModeContext.value(android.R.color.system_primary_fixed, true),
                    colors.dark.primaryFixed,
                )
                assertEquals(
                    ModeContext.value(android.R.color.system_inverse_primary_light, false),
                    colors.light.inversePrimary,
                )
                assertEquals(
                    ModeContext.value(android.R.color.system_inverse_primary_dark, true),
                    colors.dark.inversePrimary,
                )
                assertEquals(
                    ModeContext.value(android.R.color.system_surface_tint_light, false),
                    colors.light.surfaceTint,
                )
                assertEquals(
                    ModeContext.value(android.R.color.system_surface_tint_dark, true),
                    colors.dark.surfaceTint,
                )
            }
        } finally {
            ReflectionHelpers.setStaticField(Build.VERSION::class.java, "SDK_INT", original)
        }
    }

    @Test
    fun rawPaletteKeepsTheRequestedConfiguration() {
        val day = requireNotNull(dynamicPaletteOrNull(ModeContext(context, false)))
        val night = requireNotNull(dynamicPaletteOrNull(ModeContext(context, true)))
        assertNotEquals(day.accent1[Tone.T40], night.accent1[Tone.T40])
    }

    @Test
    fun readsAreEqualButNotGloballyCached() {
        val first = dynamicColorsOrNull(context)
        val second = dynamicColorsOrNull(context)
        assertEquals(first, second)
        assertNotSame(first, second)
    }

    private class ModeContext(base: Context, dark: Boolean) : ContextWrapper(base) {
        private val modeConfig =
            Configuration(base.resources.configuration).apply {
                uiMode =
                    (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
                        if (dark) Configuration.UI_MODE_NIGHT_YES
                        else Configuration.UI_MODE_NIGHT_NO
            }
        @Suppress("DEPRECATION")
        private val modeResources =
            object : Resources(base.resources.assets, base.resources.displayMetrics, modeConfig) {
                override fun getConfiguration(): Configuration = modeConfig

                override fun getColor(id: Int, theme: Theme?): Int = value(id, dark)
            }

        override fun getResources(): Resources = modeResources

        override fun createConfigurationContext(overrideConfiguration: Configuration): Context =
            ModeContext(
                baseContext,
                overrideConfiguration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                    Configuration.UI_MODE_NIGHT_YES,
            )

        companion object {
            fun value(id: Int, dark: Boolean): Int =
                0xFF000000.toInt() or (if (dark) 0x00200000 else 0x00100000) or (id and 0xffff)
        }
    }
}
