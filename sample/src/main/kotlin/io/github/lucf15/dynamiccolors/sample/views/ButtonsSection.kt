/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views

import io.github.lucf15.dynamiccolors.sample.views.components.px

internal fun NativeShowcase.buildButtonsSection() {
    heading("Buttons", "Actions")
    card {
        bind { save.colors(it) }
        save.setOnClickListener { onChange(state.copy(saved = !state.saved)) }
        addView(save, full())
        gap(18)
        addView(
            row().apply {
                addView(
                    button("Preview", false).apply { setOnClickListener { preview() } },
                    weight().apply { marginEnd = context.px(10) },
                )
                addView(
                    button("Disabled").apply {
                        isEnabled = false
                        colors(colors)
                    },
                    weight(),
                )
            },
            full(),
        )
    }
}
