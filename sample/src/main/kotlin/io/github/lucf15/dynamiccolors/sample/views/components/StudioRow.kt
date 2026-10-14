/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import kotlin.math.roundToInt

internal class StudioRow(
    context: Context,
    private val verticalGravity: Int = Gravity.CENTER_VERTICAL,
) : LinearLayout(context) {
    init {
        orientation = HORIZONTAL
        gravity = verticalGravity
        isBaselineAligned = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val children = (0 until childCount).map { getChildAt(it) }.filter { it.visibility != GONE }
        val weighted = children.filter { (it.layoutParams as LayoutParams).weight > 0f }
        if (weighted.isEmpty()) return
        val available =
            measuredWidth -
                paddingLeft -
                paddingRight -
                children.sumOf {
                    val params = it.layoutParams as LayoutParams
                    params.leftMargin +
                        params.rightMargin +
                        if (params.weight == 0f) it.measuredWidth else 0
                }
        val totalWeight =
            weighted.sumOf { (it.layoutParams as LayoutParams).weight.toDouble() }.toFloat()
        val widths = weighted.map {
            (available * (it.layoutParams as LayoutParams).weight / totalWeight).roundToInt()
        }
        var remainder = available - widths.sum()
        val original = weighted.map {
            (it.layoutParams as LayoutParams).let { p -> p.width to p.weight }
        }
        weighted.forEachIndexed { index, child ->
            val extra = remainder.compareTo(0)
            remainder -= extra
            (child.layoutParams as LayoutParams).apply {
                width = (widths[index] + extra).coerceAtLeast(0)
                weight = 0f
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        weighted.forEachIndexed { index, child ->
            (child.layoutParams as LayoutParams).apply {
                width = original[index].first
                weight = original[index].second
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            val childGravity =
                (child.layoutParams as LayoutParams).gravity.takeIf { it >= 0 } ?: verticalGravity
            if (childGravity and Gravity.VERTICAL_GRAVITY_MASK != Gravity.CENTER_VERTICAL) continue
            val free = height - paddingTop - paddingBottom - child.height
            child.offsetTopAndBottom(paddingTop + (free / 2f).roundToInt() - child.top)
        }
    }
}
