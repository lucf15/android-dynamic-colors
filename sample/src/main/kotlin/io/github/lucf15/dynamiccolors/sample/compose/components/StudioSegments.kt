/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun Segments(
    labels: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = LocalStudioColors.current
    val position by
        animateFloatAsState(
            selected.toFloat(),
            spring(dampingRatio = 0.9f, stiffness = 600f),
            label = "Selection",
        )
    Box(
        modifier
            .clip(CircleShape)
            .background(c.background)
            .border(1.dp, c.outline, CircleShape)
            .padding(4.dp),
    ) {
        Canvas(Modifier.matchParentSize()) {
            val width = size.width / labels.size
            drawRoundRect(
                c.accent,
                Offset(width * position, 0f),
                Size(width, size.height),
                CornerRadius(size.height / 2),
            )
        }
        Row(Modifier.fillMaxWidth().selectableGroup()) {
            labels.forEachIndexed { index, label ->
                Box(
                    Modifier.weight(1f)
                        .heightIn(min = 40.dp)
                        .clip(CircleShape)
                        .selectable(
                            index == selected,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Tab,
                            onClick = { onSelect(index) },
                        )
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Label(
                        label,
                        size = 13,
                        color = (if (index == selected) c.onAccent else c.muted),
                        weight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
