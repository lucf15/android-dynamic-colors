/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views

import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout.LayoutParams
import io.github.lucf15.dynamiccolors.sample.views.components.px

internal fun NativeShowcase.buildInputsSection() {
    heading("Inputs", "Preferences")
    card {
        field.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) = Unit

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    if (!updating) onChange(state.copy(name = s.toString()))
                }

                override fun afterTextChanged(s: Editable?) = Unit
            },
        )
        field.setOnFocusChangeListener { _, _ -> field.colors(colors) }
        bind { field.colors(it) }
        addView(field, full())
        gap(18)
        addView(categories, full())
        gap(18)
        addView(
            row().apply {
                minimumHeight = context.px(48)
                addView(
                    column().apply {
                        addView(label("Notifications", 15, 500))
                        addView(label("Only what matters", 12, muted = true))
                    },
                    weight(),
                )
                addView(notifications, LayoutParams(context.px(48), context.px(48)))
            },
            full(),
        )
    }
}
