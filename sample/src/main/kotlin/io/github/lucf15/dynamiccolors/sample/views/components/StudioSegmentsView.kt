/*
 * Copyright 2026 lucf15
 * SPDX-License-Identifier: Apache-2.0
 */
package io.github.lucf15.dynamiccolors.sample.views.components

import android.content.Context
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.dynamicanimation.animation.DynamicAnimation
import io.github.lucf15.dynamiccolors.sample.theme.AppColors
import kotlin.math.roundToInt

internal class StudioSegmentsView(
    context: Context,
    private val titles: List<String>,
    private val onSelected: (Int) -> Unit,
) : FrameLayout(context) {
    private val indicator = View(context)
    private val row = StudioRow(context)
    private var selected = 0
    private val labels = titles.map { title ->
        object : StudioTextView(context, title, 13, 600) {
            override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                setMeasuredDimension(
                    resolveSize(paint.measureText(text.toString()).roundToInt(), widthMeasureSpec),
                    measuredHeight,
                )
            }
        }
    }
    private val cells = labels.mapIndexed { index, label ->
        object : FrameLayout(context) {
                @Suppress("DEPRECATION")
                override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(info)
                    info.className = "android.widget.RadioButton"
                    info.isCheckable = true
                    info.isChecked = selected == index
                }

                override fun onLayout(
                    changed: Boolean,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                ) {
                    val x = ((width - label.measuredWidth) / 2f).roundToInt()
                    val y = ((height - label.measuredHeight) / 2f).roundToInt()
                    label.layout(x, y, x + label.measuredWidth, y + label.measuredHeight)
                }
            }
            .apply {
                minimumHeight = context.px(40)
                setPadding(context.px(8), context.px(10), context.px(8), context.px(10))
                contentDescription = titles[index]
                isFocusable = true
                label.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
                addView(label, LayoutParams(-2, -2))
                setOnClickListener { onSelected(index) }
            }
    }

    init {
        setPadding(context.px(4), context.px(4), context.px(4), context.px(4))
        addView(indicator, LayoutParams(0, 0))
        cells.forEach { row.addView(it, LinearLayout.LayoutParams(0, -2, 1f)) }
        addView(row, LayoutParams(-1, -2))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val previousWidth = indicator.width
        super.onLayout(changed, left, top, right, bottom)
        val cell = row.width / titles.size
        indicator.layout(paddingLeft, paddingTop, paddingLeft + cell, paddingTop + row.height)
        if (previousWidth != cell) indicator.translationX = cells[selected].left.toFloat()
    }

    fun update(index: Int, c: AppColors) {
        val changed = selected != index
        selected = index
        background = context.studioShape(c.background, c.outline, 24)
        indicator.background = context.studioShape(c.accentSurface, radius = 20)
        labels.forEachIndexed { i, view ->
            cells[i].isSelected = i == index
            view.setTextColor(if (i == index) c.onAccentSurface else c.muted)
        }
        if (changed && isLaidOut)
            NativeMotion.spring(
                indicator,
                DynamicAnimation.TRANSLATION_X,
                cells[index].left.toFloat(),
            )
    }
}
