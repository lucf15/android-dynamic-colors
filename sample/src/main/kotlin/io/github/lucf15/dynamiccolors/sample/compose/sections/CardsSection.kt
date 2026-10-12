/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.compose.components.Label
import io.github.lucf15.dynamiccolors.sample.compose.components.LocalStudioColors
import io.github.lucf15.dynamiccolors.sample.compose.components.StudioCard

@Composable
internal fun CardsSection(favorite: Boolean, onFavoriteChange: (Boolean) -> Unit) {
    val c = LocalStudioColors.current
    Section("Cards", "Collections") {
        StudioCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(40.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(c.accentSurface),
                    contentAlignment = Alignment.Center,
                ) {
                    Label("✦", size = 20, color = c.onAccentSurface)
                }
                Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Label("Weekend plans", weight = FontWeight.SemiBold)
                    Label("Personal collection", size = 12, color = c.muted)
                }
                Box(
                    Modifier.size(44.dp)
                        .clip(CircleShape)
                        .semantics { contentDescription = "Favorite collection" }
                        .toggleable(
                            favorite,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Checkbox,
                            onValueChange = { onFavoriteChange(it) },
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Label(if (favorite) "♥" else "♡", size = 24, color = c.accent)
                }
            }
            Label("A space for your next idea.", size = 20, weight = FontWeight.SemiBold)
            Label("Keep a few good things close.", color = c.muted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Design", "Places", "12 items").forEach {
                    Label(
                        it,
                        Modifier.clip(CircleShape)
                            .background(c.background)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        size = 11,
                    )
                }
            }
        }
    }
}
