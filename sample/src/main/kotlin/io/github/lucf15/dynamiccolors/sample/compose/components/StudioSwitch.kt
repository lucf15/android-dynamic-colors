/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
internal fun StudioSwitch(checked: Boolean, onChange: (Boolean) -> Unit) {
    val c = LocalStudioColors.current
    val offset by
        animateDpAsState(
            if (checked) 18.dp else 0.dp,
            spring(dampingRatio = 0.9f, stiffness = 600f),
            label = "Switch thumb",
        )
    Box(
        Modifier.size(48.dp)
            .toggleable(
                checked,
                role = Role.Switch,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onValueChange = onChange,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier.size(44.dp, 26.dp)
                .clip(CircleShape)
                .background((if (checked) c.accent else c.outline))
                .padding(3.dp),
        ) {
            Box(
                Modifier.offset { IntOffset(offset.roundToPx(), 0) }
                    .size(20.dp)
                    .clip(CircleShape)
                    .background((if (checked) c.onAccent else c.surface)),
            )
        }
    }
}
