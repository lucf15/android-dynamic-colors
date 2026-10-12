/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.views

import android.content.Context
import android.view.View
import io.github.lucf15.dynamiccolors.dynamicColorsOrNull
import io.github.lucf15.dynamiccolors.dynamicPaletteOrNull

internal class Snapshots(private val context: Context) {
    val colors by lazy(LazyThreadSafetyMode.NONE) { dynamicColorsOrNull(context) }
    val palette by lazy(LazyThreadSafetyMode.NONE) { dynamicPaletteOrNull(context) }
}

internal fun View.observeSnapshots(callback: (Snapshots) -> Unit): AutoCloseable {
    checkMainThread()
    val hub =
        getTag(R.id.dynamic_colors_observer_hub) as? ObserverHub
            ?: ObserverHub(this).also { setTag(R.id.dynamic_colors_observer_hub, it) }
    return hub.subscribe(callback)
}

internal class ObserverHub(view: View) : View.OnAttachStateChangeListener {
    private var view: View? = view
    private val bindings = linkedSetOf<Binding>()
    private var configurationSubscription: AutoCloseable? = null
    private var attached = false
    private val refresh = Runnable { deliver() }

    fun subscribe(callback: (Snapshots) -> Unit): AutoCloseable {
        val binding = Binding(this, callback)
        bindings.add(binding)
        val currentView = checkNotNull(view)
        try {
            if (bindings.size == 1) {
                currentView.addOnAttachStateChangeListener(this)
                if (currentView.isAttachedToWindow) onViewAttachedToWindow(currentView)
            } else if (attached) {
                binding.deliver(Snapshots(currentView.context))
            }
        } catch (failure: Throwable) {
            binding.close()
            throw failure
        }
        return binding
    }

    override fun onViewAttachedToWindow(view: View) {
        if (attached || bindings.isEmpty()) return
        attached = true
        configurationSubscription =
            ConfigurationUpdates.subscribe(view.context.applicationContext) {
                view.removeCallbacks(refresh)
                view.post(refresh)
            }
        deliver()
    }

    override fun onViewDetachedFromWindow(view: View) {
        attached = false
        view.removeCallbacks(refresh)
        configurationSubscription?.close()
        configurationSubscription = null
    }

    private fun deliver() {
        val currentView = view ?: return
        if (!attached) return
        val snapshots = Snapshots(currentView.context)
        var failure: Throwable? = null
        // A callback may close itself/another subscription or subscribe again.
        for (binding in bindings.toList()) {
            if (!attached) break
            try {
                binding.deliver(snapshots)
            } catch (caught: Throwable) {
                binding.close()
                if (failure == null) failure = caught
                else if (failure !== caught) failure.addSuppressed(caught)
            }
        }
        failure?.let { throw it }
    }

    private fun remove(binding: Binding) {
        bindings.remove(binding)
        if (bindings.isNotEmpty()) return
        view?.let {
            onViewDetachedFromWindow(it)
            it.removeOnAttachStateChangeListener(this)
            it.setTag(R.id.dynamic_colors_observer_hub, null)
        }
        view = null
    }

    private class Binding(
        private var hub: ObserverHub?,
        private var callback: ((Snapshots) -> Unit)?,
    ) : AutoCloseable {
        fun deliver(snapshots: Snapshots) {
            callback?.invoke(snapshots)
        }

        override fun close() {
            checkMainThread()
            val owner = hub
            hub = null
            callback = null
            owner?.remove(this)
        }
    }
}
