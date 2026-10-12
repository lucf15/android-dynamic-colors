/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

/** The thirteen system tone stops, ordered from black to white. */
public enum class Tone(public val hctTone: Int) {
    T0(0),
    T10(10),
    T20(20),
    T30(30),
    T40(40),
    T50(50),
    T60(60),
    T70(70),
    T80(80),
    T90(90),
    T95(95),
    T99(99),
    T100(100);

    /** Suffix of the corresponding Android resource: T40 is system_accent1_600. */
    public val resourceStep: Int
        get() = (100 - hctTone) * 10

    public companion object {
        /** Returns null for a tone the platform does not expose. No interpolation is performed. */
        public fun fromHctTone(value: Int): Tone? = entries.firstOrNull { it.hctTone == value }
    }
}
