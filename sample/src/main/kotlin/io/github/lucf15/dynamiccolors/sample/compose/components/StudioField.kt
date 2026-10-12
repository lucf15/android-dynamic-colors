/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun StudioField(value: String, onValueChange: (String) -> Unit) {
    val c = LocalStudioColors.current
    var focused by remember { mutableStateOf(false) }
    BasicTextField(
        value,
        { onValueChange(it) },
        singleLine = true,
        textStyle =
            TextStyle(
                fontFamily = StudioFont,
                fontSize = 15.sp,
                lineHeight = 21.sp,
                color = c.text,
            ),
        cursorBrush = SolidColor(c.accent),
        modifier =
            Modifier.fillMaxWidth()
                .heightIn(min = 53.dp)
                .onFocusChanged { focused = it.isFocused }
                .semantics { contentDescription = "Collection name" }
                .border(
                    1.dp,
                    (if (focused) c.accent else c.outline),
                    RoundedCornerShape(14.dp),
                )
                .padding(16.dp),
        decorationBox = { input ->
            Box {
                if (value.isEmpty()) Label("Collection name", color = c.muted)
                input()
            }
        },
    )
}
