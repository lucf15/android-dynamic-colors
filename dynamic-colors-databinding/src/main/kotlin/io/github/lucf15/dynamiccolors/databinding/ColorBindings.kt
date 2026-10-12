/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.databinding

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Looper
import android.view.View
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.DynamicColors
import io.github.lucf15.dynamiccolors.views.observeDynamicColors

internal enum class Target {
    Background,
    Text,
    Image,
}

internal fun View.colorBindings(): ColorBindings {
    check(Looper.myLooper() == Looper.getMainLooper()) { "Bind dynamic colors on the main thread" }
    return getTag(R.id.dynamic_colors_binding_state) as? ColorBindings
        ?: ColorBindings(this).also { setTag(R.id.dynamic_colors_binding_state, it) }
}

/** View-owned state. No global registry or captured Fragment/binding/Activity callback. */
internal class ColorBindings(private val view: View) {
    private val properties = mutableMapOf<Target, Property>()
    private var subscription: AutoCloseable? = null
    private var colors: DynamicColors? = null
    private var darkTheme: Boolean? = null

    fun setDarkTheme(value: Boolean?) {
        if (darkTheme == value) return
        darkTheme = value
        applyColors()
    }

    fun update(
        target: Target,
        role: ColorRole?,
        fallback: Int?,
        original: () -> ColorStateList?,
        apply: (ColorStateList?) -> Unit,
    ) {
        val previous = properties[target]
        if (role == null) {
            properties.remove(target)?.let { it.apply(it.original) }
            if (properties.isEmpty()) {
                subscription?.close()
                subscription = null
                colors = null
            }
            return
        }
        if (previous?.role == role && previous.fallback == fallback) return
        properties[target] =
            Property(role, fallback, if (previous != null) previous.original else original(), apply)
        if (subscription == null) {
            subscription = view.observeDynamicColors {
                colors = it
                applyColors()
            }
        }
        applyColors()
    }

    private fun applyColors() {
        val dark =
            darkTheme
                ?: (view.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                    Configuration.UI_MODE_NIGHT_YES)
        val roles = if (dark) colors?.dark else colors?.light
        properties.values.forEach { property ->
            val color = roles?.get(property.role) ?: property.fallback
            property.apply(color?.let(ColorStateList::valueOf) ?: property.original)
        }
    }

    private data class Property(
        val role: ColorRole,
        val fallback: Int?,
        val original: ColorStateList?,
        val apply: (ColorStateList?) -> Unit,
    )
}
