/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views

import android.app.Dialog
import android.widget.LinearLayout.LayoutParams
import io.github.lucf15.dynamiccolors.sample.R
import io.github.lucf15.dynamiccolors.sample.views.components.StudioButtonView
import io.github.lucf15.dynamiccolors.sample.views.components.StudioCardView
import io.github.lucf15.dynamiccolors.sample.views.components.StudioTextView
import io.github.lucf15.dynamiccolors.sample.views.components.px

internal fun NativeShowcase.preview() {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
    val card =
        StudioCardView(context).apply {
            colors(colors)
            addView(
                StudioTextView(context, "Preview", 24, 600).apply { setTextColor(colors.text) },
            )
            gap(18)
            addView(
                StudioTextView(context, "The same palette, in every detail.").apply {
                    setTextColor(colors.muted)
                },
            )
            gap(18)
            addView(
                StudioButtonView(context, "Done").apply {
                    colors(colors)
                    setOnClickListener { dialog.dismiss() }
                },
                full(),
            )
        }
    dialog.setContentView(card)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    activeDialog = dialog
    dialog.setOnDismissListener { activeDialog = null }
    dialog.show()
    dialog.window?.setLayout(
        minOf(context.px(320), rootView.width - context.px(48)),
        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    )
}
