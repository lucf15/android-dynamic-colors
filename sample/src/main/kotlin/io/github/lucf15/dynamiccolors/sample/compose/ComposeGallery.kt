/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import io.github.lucf15.dynamiccolors.sample.compose.sections.ButtonsSection
import io.github.lucf15.dynamiccolors.sample.compose.sections.CardsSection
import io.github.lucf15.dynamiccolors.sample.compose.sections.FeedbackSection
import io.github.lucf15.dynamiccolors.sample.compose.sections.InputsSection
import io.github.lucf15.dynamiccolors.sample.model.ShowcaseState

@Composable
internal fun ComposeGallery(state: ShowcaseState, onChange: (ShowcaseState) -> Unit) {
    // Callbacks retain their identity while reading the latest shared state when invoked.
    val currentState by rememberUpdatedState(state)
    val currentOnChange by rememberUpdatedState(onChange)
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        ButtonsSection(state.saved) { currentOnChange(currentState.copy(saved = it)) }
        InputsSection(
            state.name,
            state.category,
            state.notifications,
            { currentOnChange(currentState.copy(name = it)) },
            { currentOnChange(currentState.copy(category = it)) },
            { currentOnChange(currentState.copy(notifications = it)) },
        )
        CardsSection(state.favorite) { currentOnChange(currentState.copy(favorite = it)) }
        FeedbackSection(state.dismissed) { currentOnChange(currentState.copy(dismissed = it)) }
    }
}
