/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.views

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import android.widget.FrameLayout
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class ObserversTest {
    @Test
    fun subscribesOnlyWhileAttachedAndResumesOnReattach() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val activity = controller.get()
        val parent = FrameLayout(activity)
        activity.setContentView(parent)
        val view = View(activity)
        var calls = 0
        val observer = view.observeDynamicColors {
            assertNotNull(it)
            calls++
        }
        assertEquals(0, calls)
        parent.addView(view)
        assertEquals(1, calls)
        dispatchChange()
        assertEquals(2, calls)
        parent.removeView(view)
        dispatchChange()
        assertEquals(2, calls)
        parent.addView(view)
        assertEquals(3, calls)
        observer.close()
        observer.close()
        dispatchChange()
        assertEquals(3, calls)
        controller.pause().stop().destroy()
    }

    @Test
    fun closeCancelsAPendingRefresh() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val view = View(controller.get())
        controller.get().setContentView(view)
        var calls = 0
        val observer = view.observeDynamicPalette { calls++ }
        assertEquals(1, calls)
        RuntimeEnvironment.getApplication().onConfigurationChanged(Configuration())
        observer.close()
        shadowOf(android.os.Looper.getMainLooper()).idle()
        assertEquals(1, calls)
        controller.pause().stop().destroy()
    }

    @Test
    fun closingBeforeAttachmentPreventsDelivery() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val view = View(controller.get())
        var calls = 0
        view.observeDynamicColors { calls++ }.close()
        controller.get().setContentView(view)
        dispatchChange()
        assertEquals(0, calls)
        controller.pause().stop().destroy()
    }

    @Test
    fun configurationChangesAreDeliveredEvenWhenColorsAreEqual() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val view = View(controller.get())
        controller.get().setContentView(view)
        var calls = 0
        val observer = view.observeDynamicColors { calls++ }
        dispatchChange()
        assertEquals(2, calls)
        observer.close()
        controller.pause().stop().destroy()
    }

    @Test
    fun detachCancelsPendingRefreshAndClosePreventsReattachment() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val parent = FrameLayout(controller.get())
        controller.get().setContentView(parent)
        val view = View(controller.get())
        parent.addView(view)
        var calls = 0
        val observer = view.observeDynamicColors { calls++ }
        RuntimeEnvironment.getApplication().onConfigurationChanged(Configuration())
        parent.removeView(view)
        shadowOf(android.os.Looper.getMainLooper()).idle()
        assertEquals(1, calls)
        observer.close()
        parent.addView(view)
        dispatchChange()
        assertEquals(1, calls)
        controller.pause().stop().destroy()
    }

    @Test
    fun retainedClosedHandleDoesNotRetainViewOrCallback() {
        val view = View(RuntimeEnvironment.getApplication())
        val callback: (io.github.lucf15.dynamiccolors.DynamicColors?) -> Unit = { view.alpha = 1f }
        val observer = view.observeDynamicColors(callback)
        observer.close()
        // Check the library-owned object graph, including Runnable/closure captures. This tests
        // retention deterministically without relying on GC timing or Android's own View caches.
        val visited = java.util.Collections.newSetFromMap(java.util.IdentityHashMap<Any, Boolean>())
        fun inspect(value: Any) {
            assertNotSame("Closed handle retains its View", view, value)
            assertNotSame("Closed handle retains its callback", callback, value)
            if (
                !visited.add(value) ||
                    !value.javaClass.name.startsWith("io.github.lucf15.dynamiccolors.views.")
            )
                return
            value.javaClass.declaredFields
                .filterNot { java.lang.reflect.Modifier.isStatic(it.modifiers) }
                .forEach { field ->
                    field.isAccessible = true
                    field.get(value)?.let { inspect(it) }
                }
        }
        inspect(observer)
    }

    @Test
    fun throwingInitialCallbackUnregistersTheObserver() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val view = View(controller.get())
        controller.get().setContentView(view)
        var calls = 0
        assertThrows(IllegalArgumentException::class.java) {
            view.observeDynamicColors {
                calls++
                throw IllegalArgumentException("Consumer failed")
            }
        }
        dispatchChange()
        assertEquals(1, calls)
        controller.pause().stop().destroy()
    }

    @Test
    fun callbacksCanCloseOtherSubscriptionsDuringDelivery() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val view = View(controller.get())
        controller.get().setContentView(view)
        var other: AutoCloseable? = null
        var otherCalls = 0
        val first = view.observeDynamicColors { other?.close() }
        other = view.observeDynamicColors { otherCalls++ }
        dispatchChange()
        assertEquals(1, otherCalls)
        first.close()
        assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
        controller.pause().stop().destroy()
    }

    @Test
    fun failingNewSubscriberDoesNotCloseExistingSubscriber() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val view = View(controller.get())
        controller.get().setContentView(view)
        var calls = 0
        val first = view.observeDynamicColors { calls++ }
        assertThrows(IllegalStateException::class.java) {
            view.observeDynamicPalette { error("consumer") }
        }
        dispatchChange()
        assertEquals(2, calls)
        first.close()
        assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
        controller.pause().stop().destroy()
    }

    @Test
    fun selectedRolesFollowConfigurationAndAllowOverride() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val view = View(controller.get())
        controller.get().setContentView(view)
        var automatic: io.github.lucf15.dynamiccolors.ColorRoles? = null
        var forced: io.github.lucf15.dynamiccolors.ColorRoles? = null
        val first = view.observeDynamicColorRoles { automatic = it }
        val second = view.observeDynamicColorRoles(darkTheme = false) { forced = it }
        RuntimeEnvironment.setQualifiers("+night")
        dispatchChange()
        val expected =
            checkNotNull(io.github.lucf15.dynamiccolors.dynamicColorsOrNull(view.context))
        assertEquals(expected.dark, automatic)
        assertEquals(expected.light, forced)
        first.close()
        second.close()
        controller.pause().stop().destroy()
    }

    private fun dispatchChange() {
        RuntimeEnvironment.getApplication().onConfigurationChanged(Configuration())
        shadowOf(android.os.Looper.getMainLooper()).idle()
    }
}
