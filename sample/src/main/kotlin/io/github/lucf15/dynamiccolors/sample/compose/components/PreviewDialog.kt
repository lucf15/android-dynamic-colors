/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
internal fun PreviewDialog(onDismiss: () -> Unit) {
    val c = LocalStudioColors.current
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(Modifier.padding(horizontal = 24.dp).widthIn(max = 320.dp)) {
            StudioCard {
                Label("Preview", size = 24, weight = FontWeight.SemiBold)
                Label("The same palette, in every detail.", color = c.muted)
                StudioButton("Done", Modifier.fillMaxWidth()) { onDismiss() }
            }
        }
    }
}
