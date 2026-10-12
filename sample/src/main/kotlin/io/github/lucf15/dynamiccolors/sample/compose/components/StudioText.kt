/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
internal fun Label(
    text: String,
    modifier: Modifier = Modifier,
    size: Int = 15,
    color: Color = LocalStudioColors.current.text,
    weight: FontWeight = FontWeight.Normal,
) {
    BasicText(
        text,
        modifier,
        style =
            TextStyle(
                fontFamily = StudioFont,
                fontSize = size.sp,
                lineHeight = (size * 1.4).sp,
                fontWeight = weight,
                color = color,
                letterSpacing = if (size >= 28) (-0.6).sp else 0.sp,
            ),
    )
}
