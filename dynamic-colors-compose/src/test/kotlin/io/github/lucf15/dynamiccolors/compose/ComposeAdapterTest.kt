/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.compose

import android.content.res.Configuration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.v2.createComposeRule
import io.github.lucf15.dynamiccolors.DynamicColorSamples
import io.github.lucf15.dynamiccolors.DynamicColors
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ComposeAdapterTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun inspectionUsesProvidedFixturesIncludingNull() {
        var observed: DynamicColors? = DynamicColorSamples.colors()
        compose.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                DynamicColorsPreview(colors = null, palette = null) {
                    observed = rememberDynamicColorsOrNull()
                    assertNull(rememberDynamicPaletteOrNull())
                }
            }
        }
        compose.runOnIdle { assertNull(observed) }
    }

    @Test
    fun previewProviderDoesNotReplaceRuntimeColors() {
        var observed: DynamicColors? = null
        compose.setContent {
            CompositionLocalProvider(LocalInspectionMode provides false) {
                DynamicColorsPreview(colors = null, palette = null) {
                    observed = rememberDynamicColorsOrNull()
                }
            }
        }
        compose.runOnIdle { assertNotNull(observed) }
    }

    @Test
    fun aConfigurationChangeInvalidatesTheSnapshot() {
        val configuration =
            mutableStateOf(
                Configuration().apply { orientation = Configuration.ORIENTATION_PORTRAIT },
            )
        var observed: DynamicColors? = null
        compose.setContent {
            CompositionLocalProvider(LocalConfiguration provides configuration.value) {
                observed = rememberDynamicColorsOrNull()
            }
        }
        var first: DynamicColors? = null
        compose.runOnIdle {
            first = observed
            configuration.value =
                Configuration(configuration.value).apply {
                    orientation = Configuration.ORIENTATION_LANDSCAPE
                }
        }
        compose.runOnIdle {
            assertNotNull(observed)
            assertNotSame(first, observed)
        }
    }
}
