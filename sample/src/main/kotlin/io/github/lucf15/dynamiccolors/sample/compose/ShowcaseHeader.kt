/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.compose.components.Label
import io.github.lucf15.dynamiccolors.sample.compose.components.LocalStudioColors
import io.github.lucf15.dynamiccolors.sample.compose.components.Segments
import io.github.lucf15.dynamiccolors.sample.compose.components.studioClick

@Composable
internal fun ShowcaseHeader(
    dark: Boolean,
    useSystem: Boolean,
    framework: Int,
    systemAvailable: Boolean,
    topInset: androidx.compose.ui.unit.Dp,
    onDarkChange: (Boolean) -> Unit,
    onSystemChange: (Boolean) -> Unit,
    onFrameworkChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalStudioColors.current
    val background = colors.background
    Column(
        modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    0f to background,
                    0.64f to background.copy(alpha = 0.98f),
                    1f to Color.Transparent,
                ),
            )
            .padding(top = topInset + 10.dp, bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            Modifier.widthIn(max = 640.dp).fillMaxWidth().padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Label(
                    "Components",
                    Modifier.weight(1f),
                    size = 28,
                    weight = FontWeight.SemiBold,
                )
                Box(
                    Modifier.size(44.dp)
                        .semantics {
                            contentDescription =
                                if (dark) "Switch to light mode" else "Switch to dark mode"
                        }
                        .studioClick { onDarkChange(!dark) }
                        .clip(CircleShape)
                        .background(colors.surface)
                        .border(1.dp, colors.outline, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Label(if (dark) "☀" else "☾", size = 22)
                }
            }
            if (LocalDensity.current.fontScale > 1.15f) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Segments(listOf("Compose", "Views"), framework, onFrameworkChange)
                    Segments(
                        listOf("Custom", "System"),
                        if (useSystem) 1 else 0,
                        { onSystemChange(it == 1) },
                    )
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Segments(
                        listOf("Compose", "Views"),
                        framework,
                        onFrameworkChange,
                        Modifier.weight(1f),
                    )
                    Segments(
                        listOf("Custom", "System"),
                        if (useSystem) 1 else 0,
                        { onSystemChange(it == 1) },
                        Modifier.weight(1f),
                    )
                }
            }
            if (useSystem && !systemAvailable)
                Label(
                    "System colors unavailable. Using the custom palette.",
                    size = 12,
                    color = colors.muted,
                )
        }
    }
}
