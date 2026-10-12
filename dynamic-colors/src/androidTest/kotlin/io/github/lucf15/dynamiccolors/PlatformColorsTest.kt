/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

import android.content.Context
import android.content.res.Configuration
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test

@SdkSuppress(minSdkVersion = 37)
class PlatformColorsTest {
    private val context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun everyRoleMatchesTheConfiguredPlatformResource() {
        val colors = requireNotNull(dynamicColorsOrNull(context))
        for (dark in listOf(false, true)) {
            val configured = context.inMode(dark)
            val roles = if (dark) colors.dark else colors.light
            for (role in ColorRole.entries) {
                val snake = role.name.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
                val suffix = if ("Fixed" in role.name) "" else if (dark) "_dark" else "_light"
                val name = "system_$snake$suffix"
                val id = configured.resources.getIdentifier(name, "color", "android")
                assertNotEquals(name, 0, id)
                assertEquals(name, configured.getColor(id), roles[role])
            }
        }
    }

    @Test
    fun bothSetsAreIndependentOfTheCallingContextsMode() {
        assertEquals(
            dynamicColorsOrNull(context.inMode(false)),
            dynamicColorsOrNull(context.inMode(true)),
        )
    }

    @Test
    fun everyRawToneMatchesTheCallingConfiguration() {
        val names = listOf("accent1", "accent2", "accent3", "neutral1", "neutral2", "error")
        for (dark in listOf(false, true)) {
            val configured = context.inMode(dark)
            val palette = requireNotNull(dynamicPaletteOrNull(configured))
            for (family in PaletteFamily.entries) {
                val ramp = requireNotNull(palette[family])
                for (tone in Tone.entries) {
                    val name = "system_${names[family.ordinal]}_${tone.resourceStep}"
                    val id = configured.resources.getIdentifier(name, "color", "android")
                    assertNotEquals(name, 0, id)
                    assertEquals(name, configured.getColor(id), ramp[tone])
                }
            }
        }
    }

    private fun Context.inMode(dark: Boolean): Context =
        createConfigurationContext(
            Configuration(resources.configuration).apply {
                uiMode =
                    (uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()) or
                        if (dark) Configuration.UI_MODE_NIGHT_YES
                        else Configuration.UI_MODE_NIGHT_NO
            },
        )
}
