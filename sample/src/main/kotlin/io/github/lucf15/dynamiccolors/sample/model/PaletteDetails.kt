/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.model

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.Tone

internal val PaletteFamily.title: String
    get() =
        when (this) {
            PaletteFamily.Accent1 -> "Accent 1"
            PaletteFamily.Accent2 -> "Accent 2"
            PaletteFamily.Accent3 -> "Accent 3"
            PaletteFamily.Neutral1 -> "Neutral 1"
            PaletteFamily.Neutral2 -> "Neutral 2"
            PaletteFamily.Error -> "Error"
        }

internal fun PaletteFamily.resourceName(tone: Tone): String =
    "android.R.color.system_${name.lowercase()}_${tone.resourceStep}"

internal fun Int.rgbHex(): String =
    "#" + (this and 0xFFFFFF).toString(16).padStart(6, '0').uppercase()

internal fun copyPaletteColor(context: Context, family: PaletteFamily, tone: Tone, argb: Int) {
    val resource = family.resourceName(tone)
    val value = "#" + argb.toUInt().toString(16).padStart(8, '0').uppercase()
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(resource, "$resource = $value"))
    // Android 13+ supplies its own clipboard confirmation.
    if (Build.VERSION.SDK_INT < 33)
        Toast.makeText(context, "Copied $resource", Toast.LENGTH_SHORT).show()
}
