/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.compose

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.v2.createComposeRule
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.DynamicColorSamples
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.Tone
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class DynamicColorsThemeTest {
    @get:Rule val compose = createComposeRule()
    private val fallback = DynamicColorScheme { Color.Magenta }

    @Test
    fun themeSelectsModeAndRespondsToDynamicToggle() {
        val dark = mutableStateOf(false)
        val enabled = mutableStateOf(true)
        val fixture = DynamicColorSamples.colors()
        var observed: DynamicColorScheme? = null
        compose.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                DynamicColorsPreview(colors = fixture) {
                    DynamicColorsTheme(fallback, dark.value, enabled.value) {
                        observed = DynamicColorsTheme.colorScheme
                    }
                }
            }
        }
        compose.runOnIdle {
            assertEquals(fixture.light.primary, observed!!.primary.toArgb())
            dark.value = true
        }
        compose.runOnIdle {
            assertEquals(fixture.dark.primary, observed!!.primary.toArgb())
            enabled.value = false
        }
        compose.runOnIdle { assertSame(fallback, observed) }
    }

    @Test
    @Config(sdk = [28])
    fun unsupportedDeviceUsesAppFallback() {
        var observed: DynamicColorScheme? = null
        compose.setContent { observed = rememberDynamicColorScheme(fallback) }
        compose.runOnIdle { assertSame(fallback, observed) }
    }

    @Test
    fun nestedProvidersDoNotOverwriteParent() {
        val child = DynamicColorScheme { Color.Cyan }
        val observed = mutableListOf<DynamicColorScheme>()
        compose.setContent {
            DynamicColorsTheme(fallback, useDynamicColors = false) {
                observed.add(DynamicColorsTheme.colorScheme)
                DynamicColorsTheme(child, useDynamicColors = false) {
                    observed.add(DynamicColorsTheme.colorScheme)
                }
                observed.add(DynamicColorsTheme.colorScheme)
            }
        }
        compose.runOnIdle { assertEquals(listOf(fallback, child, fallback), observed) }
    }

    @Test
    fun conversionPreservesEveryRoleAndCustomSchemesKeepWideGamut() {
        val roles = DynamicColorSamples.colors().dark
        val scheme = roles.toComposeColorScheme()
        ColorRole.entries.forEach { assertEquals(roles[it], scheme[it].toArgb()) }
        val wide = Color(0.8f, 0.2f, 0.3f, colorSpace = ColorSpaces.DisplayP3)
        assertEquals(wide, DynamicColorScheme { wide }.primary)
        val palette = DynamicColorSamples.palette(includeError = false)
        assertNull(palette.color(PaletteFamily.Error, Tone.T40))
        assertEquals(
            palette.accent1[Tone.T40],
            palette.color(PaletteFamily.Accent1, Tone.T40)!!.toArgb(),
        )
    }

    @Test
    fun schemeIsRememberedAcrossUnrelatedRecomposition() {
        val frame = mutableStateOf(0)
        var observed: DynamicColorScheme? = null
        var renderedFrame = -1
        compose.setContent {
            renderedFrame = frame.value
            observed = rememberDynamicColorSchemeOrNull(darkTheme = false)
        }
        var first: DynamicColorScheme? = null
        compose.runOnIdle {
            first = observed
            frame.value++
        }
        compose.runOnIdle {
            assertEquals(1, renderedFrame)
            assertNotNull(first)
            assertSame(first, observed)
        }
    }

    @Test
    fun paletteModeOverrideDoesNotMutateHostConfiguration() {
        var observed: io.github.lucf15.dynamiccolors.DynamicPalette? = null
        val context =
            androidx.test.core.app.ApplicationProvider.getApplicationContext<
                android.content.Context,
            >()
        val original = context.resources.configuration.uiMode
        compose.setContent { observed = rememberDynamicPaletteOrNull(darkTheme = true) }
        compose.runOnIdle {
            val configuration =
                android.content.res.Configuration(context.resources.configuration).apply {
                    uiMode =
                        (uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK.inv()) or
                            android.content.res.Configuration.UI_MODE_NIGHT_YES
                }
            val expected =
                io.github.lucf15.dynamiccolors.dynamicPaletteOrNull(
                    context.createConfigurationContext(configuration),
                )
            assertEquals(expected, observed)
            assertEquals(original, context.resources.configuration.uiMode)
        }
    }

    @Test
    fun previewWithoutSystemColorsUsesFallback() {
        var observed: DynamicColorScheme? = null
        compose.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                DynamicColorsPreview(colors = null) {
                    DynamicColorsTheme(fallback) { observed = DynamicColorsTheme.colorScheme }
                }
            }
        }
        compose.runOnIdle { assertSame(fallback, observed) }
    }
}
