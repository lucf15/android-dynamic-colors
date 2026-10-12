/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.views

import android.view.View
import io.github.lucf15.dynamiccolors.ColorRoles
import io.github.lucf15.dynamiccolors.DynamicColors
import io.github.lucf15.dynamiccolors.DynamicPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

/**
 * Collects color snapshots until the calling coroutine is cancelled.
 *
 * Registration, [onChanged], and cleanup run on [Dispatchers.Main.immediate], even when called from
 * another dispatcher. Cancellation or a callback failure closes the observer; failures propagate to
 * the caller. Call from `viewLifecycleOwner.repeatOnLifecycle(STARTED)` in a Fragment.
 *
 * Delivery pauses while detached and refreshes on reattachment. Callbacks are sequential and may
 * suspend; while busy, only the latest pending snapshot is retained. Null means API < 31. Equal
 * snapshots are not filtered, so configuration changes can still update light/dark selection.
 */
public suspend fun View.collectDynamicColors(onChanged: suspend (DynamicColors?) -> Unit): Unit =
    collectSnapshots(::observeDynamicColors, onChanged)

/** Like [collectDynamicColors], but collects raw palette ramps for this View's configuration. */
public suspend fun View.collectDynamicPalette(onChanged: suspend (DynamicPalette?) -> Unit): Unit =
    collectSnapshots(::observeDynamicPalette, onChanged)

/**
 * Collects the selected mode with the same cancellation and main-thread guarantees as
 * [collectDynamicColors]. Null [darkTheme] follows this View's resource configuration.
 */
public suspend fun View.collectDynamicColorRoles(
    darkTheme: Boolean? = null,
    onChanged: suspend (ColorRoles?) -> Unit,
): Unit = collectSnapshots({ observeDynamicColorRoles(darkTheme, it) }, onChanged)

private suspend fun <T> collectSnapshots(
    observe: ((T) -> Unit) -> AutoCloseable,
    onChanged: suspend (T) -> Unit,
): Unit =
    withContext(Dispatchers.Main.immediate) {
        val updates = Channel<T>(Channel.CONFLATED)
        val subscription = observe { updates.trySend(it) }
        try {
            while (true) onChanged(updates.receive())
        } finally {
            subscription.close()
            updates.cancel()
        }
    }
