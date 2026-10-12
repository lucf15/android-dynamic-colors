/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.compose.components.PreviewDialog
import io.github.lucf15.dynamiccolors.sample.compose.components.StudioButton
import io.github.lucf15.dynamiccolors.sample.compose.components.StudioCard

@Composable
internal fun ButtonsSection(saved: Boolean, onSavedChange: (Boolean) -> Unit) {
    var preview by rememberSaveable { mutableStateOf(false) }
    Section("Buttons", "Actions") {
        StudioCard {
            StudioButton(
                if (saved) "✓  Saved" else "Save changes",
                Modifier.fillMaxWidth(),
            ) {
                onSavedChange(!saved)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StudioButton("Preview", Modifier.weight(1f), filled = false) { preview = true }
                StudioButton("Disabled", Modifier.weight(1f), enabled = false) {}
            }
        }
    }
    if (preview) PreviewDialog { preview = false }
}
