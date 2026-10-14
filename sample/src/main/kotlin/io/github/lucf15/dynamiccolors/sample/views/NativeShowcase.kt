/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.view.animation.PathInterpolator
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import io.github.lucf15.dynamiccolors.ColorRoles
import io.github.lucf15.dynamiccolors.sample.model.ShowcaseState
import io.github.lucf15.dynamiccolors.sample.theme.AppColors
import io.github.lucf15.dynamiccolors.sample.theme.DarkDefaults
import io.github.lucf15.dynamiccolors.sample.theme.LightDefaults
import io.github.lucf15.dynamiccolors.sample.theme.interpolate
import io.github.lucf15.dynamiccolors.sample.theme.toAppColors
import io.github.lucf15.dynamiccolors.sample.views.components.StudioButtonView
import io.github.lucf15.dynamiccolors.sample.views.components.StudioCardView
import io.github.lucf15.dynamiccolors.sample.views.components.StudioFieldView
import io.github.lucf15.dynamiccolors.sample.views.components.StudioRow
import io.github.lucf15.dynamiccolors.sample.views.components.StudioSegmentsView
import io.github.lucf15.dynamiccolors.sample.views.components.StudioSwitchView
import io.github.lucf15.dynamiccolors.sample.views.components.StudioTextView
import io.github.lucf15.dynamiccolors.sample.views.components.px
import io.github.lucf15.dynamiccolors.views.observeDynamicColorRoles

/** Pure View rendering of the same gallery state and app-owned components as Compose. */
internal class NativeShowcase(context: Context) : LinearLayout(context) {
    internal var state = ShowcaseState()
    internal var onChange: (ShowcaseState) -> Unit = {}
    internal var updating = false
    private var system: ColorRoles? = null
    private var useSystem = false
    private var dark = false
    internal var colors = LightDefaults
    private var target = LightDefaults
    private var paletteAnimation: ValueAnimator? = null
    private val bindings = mutableListOf<(AppColors) -> Unit>()
    internal val save = StudioButtonView(context, "Save changes")
    internal val field = StudioFieldView(context)
    internal val categories =
        StudioSegmentsView(context, listOf("Personal", "Work", "Shared")) {
            onChange(state.copy(category = it))
        }
    internal val notifications =
        StudioSwitchView(context) { onChange(state.copy(notifications = it)) }
    internal val favorite =
        object : StudioTextView(context, "♡", 24) {
            @Suppress("DEPRECATION")
            override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(info)
                info.className = "android.widget.CheckBox"
                info.isCheckable = true
                info.isChecked = state.favorite
            }
        }
    internal val error = row()
    internal val restore = StudioButtonView(context, "Show error state", false)
    internal var activeDialog: Dialog? = null
    private var subscription: AutoCloseable? = null

    init {
        orientation = VERTICAL
        buildButtonsSection()
        gap(24)
        buildInputsSection()
        gap(24)
        buildCardsSection()
        gap(24)
        buildFeedbackSection()
        applyState()
    }

    fun update(
        state: ShowcaseState,
        useSystem: Boolean,
        dark: Boolean,
        onChange: (ShowcaseState) -> Unit,
    ) {
        this.state = state
        this.onChange = onChange
        val themeChanged = this.useSystem != useSystem || this.dark != dark
        this.useSystem = useSystem
        this.dark = dark
        if (themeChanged) restartColorObservation()
        readPalette()
        applyState()
    }

    private fun applyState() {
        updating = true
        if (field.text.toString() != state.name) {
            field.setText(state.name)
            field.setSelection(state.name.length)
        }
        save.text = if (state.saved) "✓  Saved" else "Save changes"
        favorite.text = if (state.favorite) "♥" else "♡"
        categories.update(state.category, colors)
        notifications.update(state.notifications, colors)
        error.visibility = if (state.dismissed) GONE else VISIBLE
        restore.visibility = if (state.dismissed) VISIBLE else GONE
        updating = false
    }

    private fun readPalette() {
        val roles = system
        val next =
            if (useSystem && roles != null) roles.toAppColors()
            else if (dark) DarkDefaults else LightDefaults
        if (target == next) return
        target = next
        paletteAnimation?.cancel()
        if (!isLaidOut) {
            applyColors(next)
            return
        }
        val start = colors
        paletteAnimation =
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 260
                interpolator = PathInterpolator(0.4f, 0f, 0.2f, 1f)
                addUpdateListener {
                    val fraction = it.animatedValue as Float
                    applyColors(start.interpolate(next, fraction))
                }
                start()
            }
    }

    private fun applyColors(value: AppColors) {
        colors = value
        bindings.forEach { it(value) }
        applyState()
    }

    fun dispose() {
        onChange = {}
        stopColorObservation()
        paletteAnimation?.cancel()
        activeDialog?.dismiss()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        restartColorObservation()
    }

    private fun restartColorObservation() {
        stopColorObservation()
        if (useSystem && isAttachedToWindow) {
            subscription =
                observeDynamicColorRoles(darkTheme = dark) {
                    system = it
                    readPalette()
                }
        }
    }

    private fun stopColorObservation() {
        subscription?.close()
        subscription = null
        system = null
    }

    override fun onDetachedFromWindow() {
        stopColorObservation()
        paletteAnimation?.cancel()
        applyColors(target)
        activeDialog?.dismiss()
        super.onDetachedFromWindow()
    }

    internal fun bind(block: (AppColors) -> Unit) {
        bindings += block
        block(colors)
    }

    internal fun label(value: String, size: Int = 15, weight: Int = 400, muted: Boolean = false) =
        StudioTextView(context, value, size, weight).apply {
            bind { setTextColor(if (muted) it.muted else it.text) }
        }

    internal fun button(text: String, filled: Boolean = true) =
        StudioButtonView(context, text, filled).apply { bind { colors(it) } }

    internal fun heading(title: String, detail: String) {
        addView(
            row().apply {
                setPadding(0, context.px(8), 0, 0)
                addView(label(title, 17, 600), weight())
                addView(label(detail, 12, muted = true))
            },
            full(),
        )
        gap(12)
    }

    internal fun card(content: LinearLayout.() -> Unit) {
        val card = StudioCardView(context)
        bind { card.colors(it) }
        card.content()
        addView(card, full())
    }

    internal fun column() = LinearLayout(context).apply { orientation = VERTICAL }

    internal fun row() = StudioRow(context)

    internal fun full() = LayoutParams(-1, -2)

    internal fun weight() = LayoutParams(0, -2, 1f)

    internal fun LinearLayout.gap(value: Int) =
        addView(View(context), LayoutParams(1, context.px(value)))
}
