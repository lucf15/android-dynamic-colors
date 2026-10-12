/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
@file:JvmName("DynamicColorBindingAdapters")

package io.github.lucf15.dynamiccolors.databinding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.MainThread
import androidx.databinding.BindingAdapter
import io.github.lucf15.dynamiccolors.ColorRole

/**
 * Tints the existing background; null restores the tint present before this attribute took
 * ownership.
 */
@MainThread
@BindingAdapter("dynamicBackgroundTint", "dynamicBackgroundTintFallback", requireAll = false)
public fun View.bindDynamicBackgroundTint(role: ColorRole?, @ColorInt fallback: Int?) {
    colorBindings().update(Target.Background, role, fallback, { backgroundTintList }) {
        backgroundTintList = it
    }
}

/**
 * Sets semantic text color; below API 31 uses [fallback] or the original state list. Null resets
 * it.
 */
@MainThread
@BindingAdapter("dynamicTextColor", "dynamicTextColorFallback", requireAll = false)
public fun TextView.bindDynamicTextColor(role: ColorRole?, @ColorInt fallback: Int?) {
    colorBindings().update(Target.Text, role, fallback, { textColors }) {
        setTextColor(checkNotNull(it))
    }
}

/** Tints an image without replacing its drawable. Null restores its original tint. */
@MainThread
@BindingAdapter("dynamicImageTint", "dynamicImageTintFallback", requireAll = false)
public fun ImageView.bindDynamicImageTint(role: ColorRole?, @ColorInt fallback: Int?) {
    colorBindings().update(Target.Image, role, fallback, { imageTintList }) { imageTintList = it }
}

/**
 * Null follows this View's resource configuration; true/false explicitly select dark/light roles.
 */
@MainThread
@BindingAdapter("dynamicDarkTheme")
public fun View.bindDynamicDarkTheme(darkTheme: Boolean?) {
    colorBindings().setDarkTheme(darkTheme)
}
