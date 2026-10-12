/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

import org.junit.Assert.*
import org.junit.Test

class ModelsTest {
    @Test
    fun tonesMatchThePlatformSteps() {
        assertEquals(
            listOf(1000, 900, 800, 700, 600, 500, 400, 300, 200, 100, 50, 10, 0),
            Tone.entries.map { it.resourceStep },
        )
        assertNull(Tone.fromHctTone(45))
        assertEquals(Tone.T40, Tone.fromHctTone(40))
    }

    @Test
    fun colorsAreCompleteAndCallbacksAreEvaluatedOnce() {
        var calls = 0
        val roles = ColorRoles { ++calls }
        assertEquals(ColorRole.entries.size, calls)
        assertEquals(1, roles.primary)
        assertEquals(roles[ColorRole.SurfaceContainerHigh], roles.surfaceContainerHigh)
        ColorRole.entries.forEach { assertEquals(it.ordinal + 1, roles[it]) }
        assertEquals(ColorRole.entries.size, calls)
    }

    @Test
    fun rampIsASnapshotOfItsInputs() {
        var value = 0xFF123456.toInt()
        val ramp = TonalRamp { value }
        value = 0
        assertEquals(0xFF123456.toInt(), ramp[Tone.T40])
    }

    @Test
    fun modelEqualityIsStructural() {
        assertEquals(TonalRamp { it.ordinal }, TonalRamp { it.ordinal })
        assertEquals(ColorRoles { it.ordinal }, ColorRoles { it.ordinal })
        assertNotEquals(ColorRoles { it.ordinal }, ColorRoles { it.ordinal + 1 })
        assertEquals(DynamicColorSamples.colors(), DynamicColorSamples.colors())
        assertEquals(
            DynamicColorSamples.colors().hashCode(),
            DynamicColorSamples.colors().hashCode(),
        )
        assertNull(DynamicColorSamples.palette(includeError = false).error)
    }

    @Test
    fun fixtureCanRepresentDifferentFixedColorsInEachMode() {
        val colors = DynamicColors(ColorRoles { 1 }, ColorRoles { 2 })
        assertEquals(1, colors.light.primaryFixed)
        assertEquals(2, colors.dark.primaryFixed)
    }
}
