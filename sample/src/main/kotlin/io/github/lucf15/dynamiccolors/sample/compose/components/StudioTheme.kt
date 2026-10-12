/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.R
import io.github.lucf15.dynamiccolors.sample.theme.LightScheme
import io.github.lucf15.dynamiccolors.sample.theme.toStudioColors

internal val LocalStudioColors = staticCompositionLocalOf { LightScheme.toStudioColors() }
@OptIn(ExperimentalTextApi::class)
internal val StudioFont =
    FontFamily(
        *listOf(400, 500, 600, 700)
            .map {
                Font(
                    R.font.inter,
                    weight = FontWeight(it),
                    variationSettings = FontVariation.Settings(FontVariation.weight(it)),
                )
            }
            .toTypedArray(),
    )
internal val CardShape = RoundedCornerShape(22.dp)
