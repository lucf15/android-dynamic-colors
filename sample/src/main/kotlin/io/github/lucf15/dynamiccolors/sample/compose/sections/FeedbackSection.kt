/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.compose.components.Label
import io.github.lucf15.dynamiccolors.sample.compose.components.LocalStudioColors
import io.github.lucf15.dynamiccolors.sample.compose.components.StudioButton
import io.github.lucf15.dynamiccolors.sample.compose.components.studioClick

@Composable
internal fun FeedbackSection(dismissed: Boolean, onDismissedChange: (Boolean) -> Unit) {
    val c = LocalStudioColors.current
    Section("Feedback", "Error state") {
        if (!dismissed)
            Row(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(c.errorSurface)
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Label(
                        "Couldn’t save changes",
                        color = c.onErrorSurface,
                        weight = FontWeight.SemiBold,
                    )
                    Label("Please try again.", size = 12, color = c.onErrorSurface)
                }
                Label(
                    "Dismiss",
                    Modifier.studioClick { onDismissedChange(true) }.padding(12.dp),
                    size = 12,
                    color = c.onErrorSurface,
                    weight = FontWeight.SemiBold,
                )
            }
        else
            StudioButton("Show error state", Modifier.fillMaxWidth(), filled = false) {
                onDismissedChange(false)
            }
    }
}
