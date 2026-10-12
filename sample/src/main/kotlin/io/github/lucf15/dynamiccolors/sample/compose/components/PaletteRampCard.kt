/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.TonalRamp
import io.github.lucf15.dynamiccolors.Tone
import io.github.lucf15.dynamiccolors.sample.model.*

@Composable
internal fun PaletteRampCard(family: PaletteFamily, ramp: TonalRamp) {
    StudioCard {
        Label(family.title, weight = FontWeight.SemiBold)
        Tone.entries.chunked(4).forEach { tones ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tones.forEach { tone ->
                    PaletteSwatch(family, tone, ramp[tone], Modifier.weight(1f))
                }
                repeat(4 - tones.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun PaletteSwatch(family: PaletteFamily, tone: Tone, argb: Int, modifier: Modifier) {
    val context = LocalContext.current
    val c = LocalStudioColors.current
    val hex = argb.rgbHex()
    Column(
        modifier
            .semantics {
                contentDescription =
                    "${family.title}, tone ${tone.hctTone}, $hex, ${family.resourceName(tone)}"
            }
            .studioClick { copyPaletteColor(context, family, tone, argb) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            Modifier.fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(argb))
                .border(1.dp, c.outline, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Label(
                "${tone.hctTone}",
                size = 12,
                weight = FontWeight.Medium,
                color =
                    if (ColorUtils.calculateLuminance(argb) > 0.179) Color.Black else Color.White,
            )
        }
        Label(hex, size = 11, color = c.muted)
    }
}
