/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.views

import android.content.res.Configuration
import android.os.Looper
import android.view.View
import androidx.annotation.MainThread
import io.github.lucf15.dynamiccolors.ColorRoles
import io.github.lucf15.dynamiccolors.DynamicColors
import io.github.lucf15.dynamiccolors.DynamicPalette

/**
 * Delivers a fresh snapshot on attachment and resource/configuration changes, on the main thread.
 * Null means API < 31. Equal snapshots are delivered too, so callers can update light/dark
 * selection.
 *
 * Subscriptions on the same View share listeners and snapshot reads. Each handle closes
 * independently. Listening pauses on detach and resumes on reattachment. Close on the main thread
 * when permanently disposed to release the View and callback. Prefer [collectDynamicColors] inside
 * a coroutine scope.
 */
@MainThread
public fun View.observeDynamicColors(onChanged: (DynamicColors?) -> Unit): AutoCloseable =
    observeSnapshots {
        onChanged(it.colors)
    }

/** Like [observeDynamicColors], but returns raw ramps for this View's current configuration. */
@MainThread
public fun View.observeDynamicPalette(onChanged: (DynamicPalette?) -> Unit): AutoCloseable =
    observeSnapshots {
        onChanged(it.palette)
    }

/**
 * Observes only the selected mode. Null [darkTheme] follows this View's current resource
 * configuration; true/false select dark/light explicitly. Shares the same observer as
 * [observeDynamicColors] and has the same main-thread and disposal contract.
 */
@MainThread
public fun View.observeDynamicColorRoles(
    darkTheme: Boolean? = null,
    onChanged: (ColorRoles?) -> Unit,
): AutoCloseable = observeDynamicColors { colors ->
    val dark =
        darkTheme
            ?: (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES)
    onChanged(if (dark) colors?.dark else colors?.light)
}

internal fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Observe and close dynamic colors on the main thread"
    }
}
