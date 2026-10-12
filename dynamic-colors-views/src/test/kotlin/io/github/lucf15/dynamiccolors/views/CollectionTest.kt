/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.views

import android.app.Activity
import android.content.res.Configuration
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import io.github.lucf15.dynamiccolors.DynamicColors
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class CollectionTest {
    @Test
    fun cancellationAndRestartReleaseBindings() =
        runBlocking<Unit> {
            val controller = Robolectric.buildActivity(Activity::class.java).setup()
            val view = View(controller.get())
            controller.get().setContentView(view)
            var calls = 0
            repeat(3) {
                val job =
                    launch(Dispatchers.Main.immediate) {
                        view.collectDynamicColors {
                            assertSame(Looper.getMainLooper(), Looper.myLooper())
                            calls++
                        }
                    }
                assertEquals(it + 1, calls)
                job.cancelAndJoin()
                assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
                dispatchChange()
                assertEquals(it + 1, calls)
            }
            controller.pause().stop().destroy()
        }

    @Test
    fun cancellationBeforeAttachmentNeverDelivers() =
        runBlocking<Unit> {
            val controller = Robolectric.buildActivity(Activity::class.java).setup()
            val view = View(controller.get())
            val job =
                launch(Dispatchers.Main.immediate) {
                    view.collectDynamicPalette { fail("Cancelled subscription delivered") }
                }
            job.cancelAndJoin()
            controller.get().setContentView(view)
            dispatchChange()
            assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
            controller.pause().stop().destroy()
        }

    @Test
    fun callbackFailurePropagatesAndReleasesBinding() =
        runBlocking<Unit> {
            val controller = Robolectric.buildActivity(Activity::class.java).setup()
            val view = View(controller.get())
            controller.get().setContentView(view)
            val expected = IllegalStateException("consumer")
            supervisorScope {
                val task =
                    async(Dispatchers.Main.immediate) {
                        view.collectDynamicColors { throw expected }
                    }
                try {
                    task.await()
                    fail("Expected failure")
                } catch (actual: IllegalStateException) {
                    assertEquals(expected.message, actual.message)
                    assertTrue(
                        generateSequence<Throwable>(actual) { it.cause }.any { it === expected },
                    )
                }
            }
            assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
            controller.pause().stop().destroy()
        }

    @Test
    fun slowCollectorKeepsOnlyOnePendingUpdate() =
        runBlocking<Unit> {
            val controller = Robolectric.buildActivity(Activity::class.java).setup()
            val view = View(controller.get())
            controller.get().setContentView(view)
            val gate = CompletableDeferred<Unit>()
            var calls = 0
            val job =
                launch(Dispatchers.Main.immediate) {
                    view.collectDynamicPalette {
                        calls++
                        if (calls == 1) gate.await()
                    }
                }
            repeat(20) { dispatchChange() }
            assertEquals(1, calls)
            gate.complete(Unit)
            shadowOf(Looper.getMainLooper()).idle()
            assertEquals(2, calls)
            job.cancelAndJoin()
            assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
            controller.pause().stop().destroy()
        }

    @Test
    fun manySubscriptionsShareHubAndSnapshotWithIndependentCleanup() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val parent = FrameLayout(controller.get())
        controller.get().setContentView(parent)
        val view = View(controller.get())
        parent.addView(view)
        val snapshots = mutableListOf<DynamicColors?>()
        val subscriptions = List(100) { view.observeDynamicColors { snapshots.add(it) } }
        val hub = view.getTag(R.id.dynamic_colors_observer_hub)
        assertNotNull(hub)
        snapshots.clear()
        dispatchChange()
        assertEquals(100, snapshots.size)
        snapshots.forEach { assertSame(snapshots.first(), it) }
        subscriptions.take(99).forEach { it.close() }
        assertSame(hub, view.getTag(R.id.dynamic_colors_observer_hub))
        snapshots.clear()
        parent.removeView(view)
        dispatchChange()
        assertTrue(snapshots.isEmpty())
        parent.addView(view)
        assertEquals(1, snapshots.size)
        subscriptions.last().close()
        assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
        controller.pause().stop().destroy()
    }

    @Test
    fun backgroundCallerReceivesOnMainAndCanCancel() =
        runBlocking<Unit> {
            val controller = Robolectric.buildActivity(Activity::class.java).setup()
            val view = View(controller.get())
            controller.get().setContentView(view)
            val delivered = CompletableDeferred<Unit>()
            val job =
                launch(Dispatchers.Default) {
                    view.collectDynamicColors {
                        assertSame(Looper.getMainLooper(), Looper.myLooper())
                        delivered.complete(Unit)
                    }
                }
            withTimeout(5000) {
                while (!delivered.isCompleted) {
                    shadowOf(Looper.getMainLooper()).idle()
                    delay(1)
                }
                job.cancel()
                while (!job.isCompleted) {
                    shadowOf(Looper.getMainLooper()).idle()
                    delay(1)
                }
            }
            assertNull(view.getTag(R.id.dynamic_colors_observer_hub))
            controller.pause().stop().destroy()
        }

    @Test
    fun listenersAreSharedAcrossSubscriptionsAndApplicationCallbackAcrossViews() {
        val controller = Robolectric.buildActivity(Activity::class.java).setup()
        val parent = FrameLayout(controller.get())
        controller.get().setContentView(parent)
        class CountingView : View(controller.get()) {
            var listeners = 0

            override fun addOnAttachStateChangeListener(listener: OnAttachStateChangeListener) {
                listeners++
                super.addOnAttachStateChangeListener(listener)
            }

            override fun removeOnAttachStateChangeListener(listener: OnAttachStateChangeListener) {
                listeners--
                super.removeOnAttachStateChangeListener(listener)
            }
        }
        val first = CountingView()
        val second = CountingView()
        parent.addView(first)
        parent.addView(second)
        val handles =
            List(100) { first.observeDynamicColors {} } +
                first.observeDynamicPalette {} +
                second.observeDynamicColors {}
        assertEquals(1, first.listeners)
        assertEquals(1, second.listeners)
        val field = ConfigurationUpdates.javaClass.getDeclaredField("dispatchers")
        field.isAccessible = true
        val dispatchers = field.get(ConfigurationUpdates) as Map<*, *>
        assertEquals(1, dispatchers.size)
        handles.forEach { it.close() }
        assertEquals(0, first.listeners)
        assertEquals(0, second.listeners)
        assertTrue(dispatchers.isEmpty())
        controller.pause().stop().destroy()
    }

    private fun dispatchChange() {
        RuntimeEnvironment.getApplication().onConfigurationChanged(Configuration())
        shadowOf(Looper.getMainLooper()).idle()
    }
}
