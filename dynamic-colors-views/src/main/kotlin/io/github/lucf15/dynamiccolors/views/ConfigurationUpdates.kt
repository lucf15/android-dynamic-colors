/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.views

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import java.util.IdentityHashMap

/**
 * Main-thread registry containing only attached observers; one platform callback per application.
 */
internal object ConfigurationUpdates {
    private val dispatchers = IdentityHashMap<Context, Dispatcher>()

    fun subscribe(application: Context, callback: () -> Unit): AutoCloseable {
        val dispatcher =
            dispatchers[application]
                ?: Dispatcher().also {
                    application.registerComponentCallbacks(it)
                    dispatchers[application] = it
                }
        dispatcher.callbacks.add(callback)
        return object : AutoCloseable {
            private var active = true

            override fun close() {
                if (!active) return
                active = false
                dispatcher.callbacks.remove(callback)
                if (dispatcher.callbacks.isEmpty()) {
                    application.unregisterComponentCallbacks(dispatcher)
                    dispatchers.remove(application)
                }
            }
        }
    }

    private class Dispatcher : ComponentCallbacks {
        val callbacks = linkedSetOf<() -> Unit>()

        override fun onConfigurationChanged(newConfig: Configuration) {
            callbacks.toList().forEach { it() }
        }

        @Deprecated("Required by ComponentCallbacks") override fun onLowMemory() = Unit
    }
}
