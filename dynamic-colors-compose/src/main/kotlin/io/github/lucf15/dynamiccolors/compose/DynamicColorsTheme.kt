/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import io.github.lucf15.dynamiccolors.DynamicPalette
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.Tone

/** A remembered Compose-native scheme for the selected mode, or null below API 31. */
@Composable
public fun rememberDynamicColorSchemeOrNull(
    darkTheme: Boolean = isSystemInDarkTheme(),
): DynamicColorScheme? {
    val colors = rememberDynamicColorsOrNull()
    val roles = if (darkTheme) colors?.dark else colors?.light
    return remember(roles) { roles?.toComposeColorScheme() }
}

/** Resolves system colors, using the app's [fallback] on unsupported devices. */
@Composable
public fun rememberDynamicColorScheme(
    fallback: DynamicColorScheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
): DynamicColorScheme = rememberDynamicColorSchemeOrNull(darkTheme) ?: fallback

/**
 * Reads system colors once for this subtree and exposes them through
 * [DynamicColorsTheme.colorScheme]. Supply the app's fallback for the selected mode. Set
 * [useDynamicColors] to false to use it on every device. This provider does not install a Material
 * theme; it works with any Compose design system. Preview fixtures supplied by
 * [DynamicColorsPreview] are honored.
 */
@Composable
public fun DynamicColorsTheme(
    fallback: DynamicColorScheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = true,
    content: @Composable () -> Unit,
) {
    val scheme = if (useDynamicColors) rememberDynamicColorScheme(fallback, darkTheme) else fallback
    CompositionLocalProvider(LocalColorScheme provides scheme, content = content)
}

public object DynamicColorsTheme {
    /** Colors from the nearest [DynamicColorsTheme] provider. */
    public val colorScheme: DynamicColorScheme
        @Composable @ReadOnlyComposable get() = LocalColorScheme.current
}

private val LocalColorScheme =
    staticCompositionLocalOf<DynamicColorScheme> {
        error("Wrap this content in DynamicColorsTheme and supply your app's fallback colors")
    }

/**
 * Converts a palette tone, or returns null when the family is unavailable (for example error < 35).
 */
public fun DynamicPalette.color(family: PaletteFamily, tone: Tone): Color? =
    this[family]?.color(tone)
