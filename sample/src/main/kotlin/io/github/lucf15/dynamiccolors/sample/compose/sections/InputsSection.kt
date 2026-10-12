/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.sections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.compose.components.Label
import io.github.lucf15.dynamiccolors.sample.compose.components.LocalStudioColors
import io.github.lucf15.dynamiccolors.sample.compose.components.Segments
import io.github.lucf15.dynamiccolors.sample.compose.components.StudioCard
import io.github.lucf15.dynamiccolors.sample.compose.components.StudioField
import io.github.lucf15.dynamiccolors.sample.compose.components.StudioSwitch

@Composable
internal fun InputsSection(
    name: String,
    category: Int,
    notifications: Boolean,
    onNameChange: (String) -> Unit,
    onCategoryChange: (Int) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
) {
    val c = LocalStudioColors.current
    Section("Inputs", "Preferences") {
        StudioCard {
            StudioField(name, onNameChange)
            Segments(
                listOf("Personal", "Work", "Shared"),
                category,
                { onCategoryChange(it) },
            )
            Row(
                Modifier.fillMaxWidth().heightIn(min = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Label("Notifications", weight = FontWeight.Medium)
                    Label("Only what matters", size = 12, color = c.muted)
                }
                Box(Modifier.semantics { contentDescription = "Notifications" }) {
                    StudioSwitch(notifications) {
                        onNotificationsChange(it)
                    }
                }
            }
        }
    }
}
