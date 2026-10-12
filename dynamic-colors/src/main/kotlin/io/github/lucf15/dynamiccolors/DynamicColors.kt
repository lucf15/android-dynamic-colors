/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
@file:JvmName("AndroidDynamicColors")

package io.github.lucf15.dynamiccolors

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import io.github.lucf15.dynamiccolors.internal.AndroidColors

/**
 * Complete light and dark sets, each resolved with its own resource configuration. Fixed roles are
 * not guaranteed equal between modes on Android 17.
 *
 * On API 34+, roles come from platform resources. On API 31–33 they are derived from the raw
 * palette: unavailable surface tones use the nearest real stop and error roles use the Material 3
 * baseline. Inverse/tint roles are aliases and scrim is black until dedicated resources arrive
 * at 37. These choices favor platform values and a small dependency footprint over exact Material 3
 * parity.
 */
public data class DynamicColors(public val light: ColorRoles, public val dark: ColorRoles)

/**
 * Reads a fresh snapshot for [context], or null below API 31. Does not cache or observe changes.
 * Null means the caller should use its own colors. Resource failures are not hidden as null.
 */
public fun dynamicColorsOrNull(context: Context): DynamicColors? =
    if (Build.VERSION.SDK_INT >= 31) AndroidColors.colors(context) else null

/** Reads the raw palette for [context]'s current configuration, or null below API 31. */
public fun dynamicPaletteOrNull(context: Context): DynamicPalette? =
    if (Build.VERSION.SDK_INT >= 31) AndroidColors.palette(context) else null

/** Whether the platform exposes the required resources; not a test of wallpaper personalization. */
@ChecksSdkIntAtLeast(api = 31)
public fun isDynamicColorAvailable(): Boolean = Build.VERSION.SDK_INT >= 31
