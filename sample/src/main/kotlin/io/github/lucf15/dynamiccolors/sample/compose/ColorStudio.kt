/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import io.github.lucf15.dynamiccolors.compose.DynamicColorsTheme
import io.github.lucf15.dynamiccolors.sample.model.ShowcaseState
import io.github.lucf15.dynamiccolors.sample.theme.DarkScheme
import io.github.lucf15.dynamiccolors.sample.theme.LightScheme

private val ShowcaseSaver =
    listSaver<ShowcaseState, Any>(
        save = {
            listOf(it.name, it.category, it.notifications, it.saved, it.favorite, it.dismissed)
        },
        restore = {
            ShowcaseState(
                it[0] as String,
                it[1] as Int,
                it[2] as Boolean,
                it[3] as Boolean,
                it[4] as Boolean,
                it[5] as Boolean,
            )
        },
    )

@Composable
internal fun ColorStudio() {
    val deviceDark = isSystemInDarkTheme()
    var dark by rememberSaveable { mutableStateOf(deviceDark) }
    var useSystem by rememberSaveable { mutableStateOf(false) }
    var framework by rememberSaveable { mutableIntStateOf(0) }
    var state by rememberSaveable(stateSaver = ShowcaseSaver) { mutableStateOf(ShowcaseState()) }
    DynamicColorsTheme(
        fallback = if (dark) DarkScheme else LightScheme,
        darkTheme = dark,
        useDynamicColors = useSystem,
    ) {
        StudioContent(
            state,
            dark,
            useSystem,
            framework,
            onStateChange = { state = it },
            onDarkChange = { dark = it },
            onSystemChange = { useSystem = it },
            onFrameworkChange = { framework = it },
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(name = "Light", widthDp = 393, heightDp = 852)
@androidx.compose.ui.tooling.preview.Preview(
    name = "Dark",
    widthDp = 393,
    heightDp = 852,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun StudioPreview() {
    io.github.lucf15.dynamiccolors.compose.DynamicColorsPreview { ColorStudio() }
}
