/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors

import io.github.lucf15.dynamiccolors.internal.deriveRoles

/** Deterministic synthetic fixtures. The system readers never use these as a fallback. */
public object DynamicColorSamples {
    /**
     * Synthetic ramps for a preview or test. These are HSL samples, not colorimetrically exact HCT.
     */
    public fun palette(includeError: Boolean = true): DynamicPalette =
        DynamicPalette(
            accent1 = TonalRamp { accent1[it.ordinal] },
            accent2 = TonalRamp { accent2[it.ordinal] },
            accent3 = TonalRamp { accent3[it.ordinal] },
            neutral1 = TonalRamp { neutral1[it.ordinal] },
            neutral2 = TonalRamp { neutral2[it.ordinal] },
            error = if (includeError) TonalRamp { error[it.ordinal] } else null,
        )

    /** Complete sample colors in both modes, using the documented API 31 compatibility mapping. */
    public fun colors(): DynamicColors {
        val palette = palette()
        return DynamicColors(deriveRoles(palette, false), deriveRoles(palette, true))
    }

    private val accent1 =
        intArrayOf(
            0xFF000000.toInt(),
            0xFF140F24.toInt(),
            0xFF291F47.toInt(),
            0xFF3D2E6B.toInt(),
            0xFF523D8F.toInt(),
            0xFF664DB3.toInt(),
            0xFF8570C2.toInt(),
            0xFFA394D1.toInt(),
            0xFFC2B8E0.toInt(),
            0xFFE0DBF0.toInt(),
            0xFFF0EDF7.toInt(),
            0xFFFCFBFD.toInt(),
            0xFFFFFFFF.toInt(),
        )

    private val accent2 =
        intArrayOf(
            0xFF000000.toInt(),
            0xFF19151E.toInt(),
            0xFF322B3B.toInt(),
            0xFF4A4059.toInt(),
            0xFF635676.toInt(),
            0xFF7C6B94.toInt(),
            0xFF9689A9.toInt(),
            0xFFB0A6BF.toInt(),
            0xFFCBC4D4.toInt(),
            0xFFE5E1EA.toInt(),
            0xFFF2F0F4.toInt(),
            0xFFFCFCFD.toInt(),
            0xFFFFFFFF.toInt(),
        )

    private val accent3 =
        intArrayOf(
            0xFF000000.toInt(),
            0xFF20131C.toInt(),
            0xFF3F2737.toInt(),
            0xFF5F3A53.toInt(),
            0xFF7E4E6E.toInt(),
            0xFF9E618A.toInt(),
            0xFFB181A1.toInt(),
            0xFFC5A0B9.toInt(),
            0xFFD8C0D0.toInt(),
            0xFFECDFE8.toInt(),
            0xFFF5EFF3.toInt(),
            0xFFFDFCFD.toInt(),
            0xFFFFFFFF.toInt(),
        )

    private val neutral1 =
        intArrayOf(
            0xFF000000.toInt(),
            0xFF19191A.toInt(),
            0xFF323135.toInt(),
            0xFF4C4A4F.toInt(),
            0xFF656369.toInt(),
            0xFF7E7C83.toInt(),
            0xFF98969C.toInt(),
            0xFFB2B0B5.toInt(),
            0xFFCBCACE.toInt(),
            0xFFE5E5E6.toInt(),
            0xFFF2F2F3.toInt(),
            0xFFFCFCFD.toInt(),
            0xFFFFFFFF.toInt(),
        )

    private val neutral2 =
        intArrayOf(
            0xFF000000.toInt(),
            0xFF19181B.toInt(),
            0xFF323036.toInt(),
            0xFF4B4851.toInt(),
            0xFF64606C.toInt(),
            0xFF7D7887.toInt(),
            0xFF97939F.toInt(),
            0xFFB1AEB7.toInt(),
            0xFFCBC9CF.toInt(),
            0xFFE5E4E7.toInt(),
            0xFFF2F1F3.toInt(),
            0xFFFCFCFD.toInt(),
            0xFFFFFFFF.toInt(),
        )

    private val error =
        intArrayOf(
            0xFF000000.toInt(),
            0xFF2D1606.toInt(),
            0xFF592D0D.toInt(),
            0xFF864313.toInt(),
            0xFFB35919.toInt(),
            0xFFDF7020.toInt(),
            0xFFE68C4C.toInt(),
            0xFFECA979.toInt(),
            0xFFF2C6A6.toInt(),
            0xFFF9E2D2.toInt(),
            0xFFFCF1E9.toInt(),
            0xFFFEFCFB.toInt(),
            0xFFFFFFFF.toInt(),
        )
}
