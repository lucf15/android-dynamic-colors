/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.model

/** State belongs to the gallery, so switching renderers never resets a component. */
internal data class ShowcaseState(
    val name: String = "",
    val category: Int = 0,
    val notifications: Boolean = true,
    val saved: Boolean = false,
    val favorite: Boolean = false,
    val dismissed: Boolean = false,
)
