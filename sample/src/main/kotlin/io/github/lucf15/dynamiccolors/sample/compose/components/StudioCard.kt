/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
internal fun StudioCard(content: @Composable ColumnScope.() -> Unit) {
    val c = LocalStudioColors.current
    Column(
        Modifier.fillMaxWidth()
            .clip(CardShape)
            .background(c.surface)
            .border(1.dp, c.outline, CardShape)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        content = content,
    )
}
