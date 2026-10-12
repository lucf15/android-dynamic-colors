/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views

import android.view.Gravity
import android.widget.LinearLayout.LayoutParams
import io.github.lucf15.dynamiccolors.sample.views.components.NativeMotion
import io.github.lucf15.dynamiccolors.sample.views.components.px
import io.github.lucf15.dynamiccolors.sample.views.components.studioShape

internal fun NativeShowcase.buildCardsSection() {
    heading("Cards", "Collections")
    card {
        addView(
            row().apply {
                addView(
                    label("✦", 20).apply {
                        gravity = Gravity.CENTER
                        bind { c ->
                            background = context.studioShape(c.accentSurface, radius = 14)
                            setTextColor(c.onAccentSurface)
                        }
                    },
                    LayoutParams(context.px(40), context.px(40)),
                )
                addView(
                    column().apply {
                        setPadding(context.px(12), 0, context.px(12), 0)
                        addView(label("Weekend plans", 15, 600))
                        addView(label("Personal collection", 12, muted = true))
                    },
                    weight(),
                )
                favorite.gravity = Gravity.CENTER
                favorite.contentDescription = "Favorite collection"
                favorite.isFocusable = true
                NativeMotion.press(favorite)
                favorite.setOnClickListener { onChange(state.copy(favorite = !state.favorite)) }
                bind { favorite.setTextColor(it.accent) }
                addView(favorite, LayoutParams(context.px(44), context.px(44)))
            },
            full(),
        )
        gap(18)
        addView(label("A space for your next idea.", 20, 600))
        gap(18)
        addView(label("Keep a few good things close.", 15, muted = true))
        gap(18)
        addView(
            row().apply {
                listOf("Design", "Places", "12 items").forEachIndexed { index, value ->
                    addView(
                        label(value, 11).apply {
                            setPadding(
                                context.px(12),
                                context.px(8),
                                context.px(12),
                                context.px(8),
                            )
                            bind {
                                background = context.studioShape(it.background, radius = 24)
                            }
                        },
                        LayoutParams(-2, -2).apply {
                            if (index > 0) marginStart = context.px(8)
                        },
                    )
                }
            },
            full(),
        )
    }
}
