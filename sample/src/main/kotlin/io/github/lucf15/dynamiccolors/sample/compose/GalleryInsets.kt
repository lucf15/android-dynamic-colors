/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/** Extra scroll range for the keyboard, without shrinking or clipping the drawing viewport. */
@Composable
internal fun rememberGalleryPadding(header: Dp): PaddingValues {
    val bottomInsets = WindowInsets.ime.union(WindowInsets.navigationBars)
    val density = LocalDensity.current
    return remember(header, bottomInsets, density) {
        object : PaddingValues {
            override fun calculateLeftPadding(layoutDirection: LayoutDirection) = 24.dp

            override fun calculateRightPadding(layoutDirection: LayoutDirection) = 24.dp

            override fun calculateTopPadding() = header

            // Read during measurement so IME animation invalidates layout, not the whole screen.
            override fun calculateBottomPadding() =
                with(density) { bottomInsets.getBottom(this).toDp() } + 40.dp
        }
    }
}
