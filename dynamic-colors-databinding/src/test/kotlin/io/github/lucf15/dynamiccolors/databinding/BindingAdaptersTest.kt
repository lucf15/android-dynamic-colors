/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.databinding

import android.app.Activity
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.github.lucf15.dynamiccolors.ColorRole
import io.github.lucf15.dynamiccolors.databinding.databinding.ColorBindingTestBinding
import io.github.lucf15.dynamiccolors.dynamicColorsOrNull
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class BindingAdaptersTest {
    @Test
    fun generatedXmlRebindsModeAndRoleAndResets() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val binding = ColorBindingTestBinding.inflate(LayoutInflater.from(controller.get()))
        controller.get().setContentView(binding.root)
        val colors = checkNotNull(dynamicColorsOrNull(controller.get()))
        binding.role = ColorRole.Primary
        binding.dark = false
        binding.executePendingBindings()
        assertEquals(colors.light.primary, binding.label.currentTextColor)
        binding.dark = true
        binding.role = ColorRole.Secondary
        binding.executePendingBindings()
        assertEquals(colors.dark.secondary, binding.label.currentTextColor)
        assertEquals(colors.dark.secondary, binding.label.backgroundTintList!!.defaultColor)
        binding.role = null
        binding.executePendingBindings()
        assertEquals(0xff123456.toInt(), binding.label.currentTextColor)
        assertNull(binding.label.backgroundTintList)
        assertNull(
            binding.root.getTag(
                io.github.lucf15.dynamiccolors.views.R.id.dynamic_colors_observer_hub,
            ),
        )
        controller.pause().stop().destroy()
    }

    @Test
    fun repeatedBindingDoesNotAccumulateListenersAndDetachPauses() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val parent = FrameLayout(controller.get())
        controller.get().setContentView(parent)
        class CountingText : TextView(controller.get()) {
            var listeners = 0

            override fun addOnAttachStateChangeListener(
                listener: View.OnAttachStateChangeListener,
            ) {
                listeners++
                super.addOnAttachStateChangeListener(listener)
            }

            override fun removeOnAttachStateChangeListener(
                listener: View.OnAttachStateChangeListener,
            ) {
                listeners--
                super.removeOnAttachStateChangeListener(listener)
            }
        }
        val view = CountingText()
        parent.addView(view)
        repeat(100) {
            view.bindDynamicTextColor(
                if (it % 2 == 0) ColorRole.Primary else ColorRole.Secondary,
                null,
            )
            view.bindDynamicBackgroundTint(ColorRole.Surface, null)
        }
        assertEquals(1, view.listeners)
        parent.removeView(view)
        view.setTextColor(Color.MAGENTA)
        RuntimeEnvironment.getApplication().onConfigurationChanged(Configuration())
        shadowOf(Looper.getMainLooper()).idle()
        assertEquals(Color.MAGENTA, view.currentTextColor)
        parent.addView(view)
        assertNotEquals(Color.MAGENTA, view.currentTextColor)
        view.bindDynamicTextColor(null, null)
        assertEquals(1, view.listeners)
        view.bindDynamicBackgroundTint(null, null)
        assertEquals(0, view.listeners)
        controller.pause().stop().destroy()
    }

    @Test
    fun clearingRolesRestoresOriginalStateListsAndNullTintsAfterRebinding() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val text = TextView(controller.get())
        val original =
            ColorStateList(
                arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf()),
                intArrayOf(Color.GRAY, Color.BLUE),
            )
        text.setTextColor(original)
        controller.get().setContentView(text)
        text.bindDynamicTextColor(ColorRole.Primary, null)
        text.bindDynamicTextColor(ColorRole.Secondary, null)
        text.bindDynamicBackgroundTint(ColorRole.Surface, null)
        text.bindDynamicBackgroundTint(ColorRole.Primary, null)
        text.bindDynamicTextColor(null, null)
        text.bindDynamicBackgroundTint(null, null)
        assertSame(original, text.textColors)
        assertNull(text.backgroundTintList)
        val image = ImageView(controller.get())
        controller.get().setContentView(image)
        image.bindDynamicImageTint(ColorRole.Primary, null)
        image.bindDynamicImageTint(ColorRole.Secondary, null)
        image.bindDynamicImageTint(null, null)
        assertNull(image.imageTintList)
        controller.pause().stop().destroy()
    }

    @Test
    @Config(sdk = [28])
    fun unsupportedDeviceUsesFallbackIncludingTransparent() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val text = TextView(controller.get())
        text.setTextColor(Color.BLUE)
        controller.get().setContentView(text)
        text.bindDynamicTextColor(ColorRole.Primary, null)
        assertEquals(Color.BLUE, text.currentTextColor)
        text.bindDynamicTextColor(ColorRole.Primary, Color.TRANSPARENT)
        assertEquals(Color.TRANSPARENT, text.currentTextColor)
        text.bindDynamicTextColor(null, null)
        assertEquals(Color.BLUE, text.currentTextColor)
        controller.pause().stop().destroy()
    }
}
