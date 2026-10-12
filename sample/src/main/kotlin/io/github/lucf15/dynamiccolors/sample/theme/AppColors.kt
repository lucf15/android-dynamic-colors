/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.theme

import io.github.lucf15.dynamiccolors.ColorRoles

/** App-owned design tokens. Only this mapping knows about the library's semantic roles. */
internal data class AppColors(
    val background: Int,
    val surface: Int,
    val text: Int,
    val muted: Int,
    val accent: Int,
    val onAccent: Int,
    val outline: Int,
    val accentSurface: Int,
    val onAccentSurface: Int,
    val secondary: Int,
    val error: Int,
    val errorSurface: Int,
    val onErrorSurface: Int,
)

internal val LightDefaults =
    AppColors(
        background = 0xFFF5F5F7.toInt(),
        surface = 0xFFFFFFFF.toInt(),
        text = 0xFF17181C.toInt(),
        muted = 0xFF696B73.toInt(),
        accent = 0xFF2563EB.toInt(),
        onAccent = 0xFFFFFFFF.toInt(),
        outline = 0xFFE2E3E7.toInt(),
        accentSurface = 0xFFE2EAFF.toInt(),
        onAccentSurface = 0xFF163776.toInt(),
        secondary = 0xFF6975A3.toInt(),
        error = 0xFFBA2638.toInt(),
        errorSurface = 0xFFFFE9EC.toInt(),
        onErrorSurface = 0xFF8C1728.toInt(),
    )
internal val DarkDefaults =
    AppColors(
        background = 0xFF0C0D10.toInt(),
        surface = 0xFF18191E.toInt(),
        text = 0xFFE9EAF0.toInt(),
        muted = 0xFFA0A1AA.toInt(),
        accent = 0xFF9CBBFF.toInt(),
        onAccent = 0xFF092B69.toInt(),
        outline = 0xFF2D2F36.toInt(),
        accentSurface = 0xFF1A3058.toInt(),
        onAccentSurface = 0xFFD8E5FF.toInt(),
        secondary = 0xFFB8BFE9.toInt(),
        error = 0xFFFFAAB4.toInt(),
        errorSurface = 0xFF3F1C26.toInt(),
        onErrorSurface = 0xFFFFD9DF.toInt(),
    )

internal fun ColorRoles.toAppColors() =
    AppColors(
        background = background,
        surface = surfaceContainerLow,
        text = onSurface,
        muted = onSurfaceVariant,
        accent = primary,
        onAccent = onPrimary,
        outline = outlineVariant,
        accentSurface = primaryContainer,
        onAccentSurface = onPrimaryContainer,
        secondary = tertiary,
        error = error,
        errorSurface = errorContainer,
        onErrorSurface = onErrorContainer,
    )
