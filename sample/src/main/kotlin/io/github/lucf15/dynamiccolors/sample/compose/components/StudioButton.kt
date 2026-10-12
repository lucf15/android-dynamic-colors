/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun StudioButton(
    text: String,
    modifier: Modifier = Modifier,
    filled: Boolean = true,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val c = LocalStudioColors.current
    Box(
        modifier
            .heightIn(min = 50.dp)
            .alpha(if (enabled) 1f else 0.38f)
            .studioClick(enabled, onClick)
            .clip(CircleShape)
            .background((if (filled) c.accent else c.surface))
            .border(1.dp, (if (filled) c.accent else c.outline), CircleShape)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Label(text, color = (if (filled) c.onAccent else c.text), weight = FontWeight.SemiBold)
    }
}
