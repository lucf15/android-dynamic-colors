/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.compose.components.Label
import io.github.lucf15.dynamiccolors.sample.compose.components.LocalStudioColors

@Composable
internal fun ColorSourceNote(useSystem: Boolean, available: Boolean) {
    val system = useSystem && available
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Label(
            if (system) "Android system palette" else "Custom palette",
            size = 17,
            weight = FontWeight.SemiBold,
        )
        Label(
            if (system)
                "Android’s color roles mapped to the app’s own components. Only the colors change."
            else
                "App-defined colors for light and dark mode. Switch to System to use the device palette.",
            size = 12,
            color = LocalStudioColors.current.muted,
        )
    }
}
