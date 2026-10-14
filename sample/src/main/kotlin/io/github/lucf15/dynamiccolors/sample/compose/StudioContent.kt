/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.lucf15.dynamiccolors.PaletteFamily
import io.github.lucf15.dynamiccolors.compose.DynamicColorsTheme
import io.github.lucf15.dynamiccolors.compose.rememberDynamicPaletteOrNull
import io.github.lucf15.dynamiccolors.isDynamicColorAvailable
import io.github.lucf15.dynamiccolors.sample.compose.components.LocalStudioColors
import io.github.lucf15.dynamiccolors.sample.compose.components.PaletteRampCard
import io.github.lucf15.dynamiccolors.sample.compose.components.animatedPalette
import io.github.lucf15.dynamiccolors.sample.compose.sections.PaletteHeading
import io.github.lucf15.dynamiccolors.sample.model.ShowcaseState
import io.github.lucf15.dynamiccolors.sample.theme.toAppColors
import io.github.lucf15.dynamiccolors.sample.theme.toStudioColors
import io.github.lucf15.dynamiccolors.sample.views.NativeShowcase
import io.github.lucf15.dynamiccolors.sample.views.PaletteRampView

@Composable
internal fun StudioContent(
    state: ShowcaseState,
    dark: Boolean,
    useSystem: Boolean,
    framework: Int,
    onStateChange: (ShowcaseState) -> Unit,
    onDarkChange: (Boolean) -> Unit,
    onSystemChange: (Boolean) -> Unit,
    onFrameworkChange: (Int) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val hostView = LocalView.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scheme = DynamicColorsTheme.colorScheme
    val target = remember(scheme) { scheme.toStudioColors() }
    val palette = rememberDynamicPaletteOrNull(darkTheme = dark)
    val families = remember(palette) { PaletteFamily.entries.filter { palette?.get(it) != null } }
    val colors = animatedPalette(target)
    val nativeColors = remember(colors) { colors.toAppColors() }
    val activity = LocalActivity.current as? ComponentActivity
    LaunchedEffect(activity, dark) {
        val style =
            if (dark) SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
            else
                SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                )
        activity?.enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
    }
    val topInset =
        WindowInsets.statusBars
            .union(WindowInsets.displayCutout)
            .asPaddingValues()
            .calculateTopPadding()
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var headerHeight by remember { mutableIntStateOf(0) }
    val headerInset =
        if (headerHeight == 0) topInset + 142.dp
        else with(LocalDensity.current) { headerHeight.toDp() }
    val background = colors.background
    CompositionLocalProvider(LocalStudioColors provides colors) {
        Box(Modifier.fillMaxSize().background(background)) {
            LazyColumn(
                Modifier.widthIn(max = 640.dp).fillMaxSize().align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = rememberGalleryPadding(headerInset),
            ) {
                item(key = "color-source") {
                    ColorSourceNote(useSystem, isDynamicColorAvailable())
                }
                item(key = "gallery") {
                    // One stable list item gives both renderers the same scroll coordinates.
                    if (framework == 0) ComposeGallery(state, onStateChange)
                    else
                        AndroidView(
                            factory = { NativeShowcase(it) },
                            modifier = Modifier.fillMaxWidth(),
                            onRelease = { it.dispose() },
                            update = { it.update(state, useSystem, dark, onStateChange) },
                        )
                }
                item(key = "palette-heading") { PaletteHeading(palette, dark) }
                items(families, key = { "palette-${it.name}" }) { family ->
                    val ramp = requireNotNull(palette?.get(family))
                    if (framework == 0) PaletteRampCard(family, ramp)
                    else
                        AndroidView(
                            factory = { PaletteRampView(it) },
                            modifier = Modifier.fillMaxWidth(),
                            update = { it.update(family, ramp, nativeColors) },
                        )
                }
            }
            ShowcaseHeader(
                dark,
                useSystem,
                framework,
                isDynamicColorAvailable(),
                topInset,
                onDarkChange,
                onSystemChange,
                onFrameworkChange = {
                    // Clear focus before removing the native field from the lazy list.
                    focusManager.clearFocus(force = true)
                    hostView.findFocus()?.clearFocus()
                    keyboard?.hide()
                    onFrameworkChange(it)
                },
                modifier =
                    Modifier.align(Alignment.TopCenter).onSizeChanged { headerHeight = it.height },
            )
            Box(
                Modifier.align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(bottomInset + 32.dp)
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.7f to background.copy(alpha = 0.92f),
                            1f to background,
                        ),
                    ),
            )
        }
    }
}
