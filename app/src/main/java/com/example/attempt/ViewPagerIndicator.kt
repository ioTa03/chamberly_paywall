package com.example.attempt

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import kotlin.math.abs

class ViewPagerIndicator(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private var dotViews = mutableListOf<View>()
    private var selectedPosition = 0
    private val largeDotSize = 10 // Size of large dots in dp
    private val mediumDotSize = 8 // Size of medium dots in dp
    private val smallDotSize = 6// Size of small dots in dp
    private val dotSpacing = 8// Spacing between dots in dp
    private var dotCount = 0
    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    fun setIndicatorCount(count: Int) {
        removeAllViews()
        dotViews.clear()
        dotCount = count

        for (i in 0 until count) {
            val dot = View(context)
            dot.setBackgroundResource(R.drawable.dot_background)
            val params = LayoutParams(
                smallDotSize.dpToPx(),
                smallDotSize.dpToPx()
            )
            params.marginEnd = dotSpacing.dpToPx()
            addView(dot, params)
            dotViews.add(dot)
        }
        setSelectedPosition(0)
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        dotViews.forEachIndexed { index, dot ->
            val size = when {
                index == position -> largeDotSize
                abs(index - position) == 1 -> mediumDotSize
                else -> smallDotSize
//                if u want circular too
//                index == position % dotCount -> largeDotSize
//                abs(index - position % dotCount) == 1 ||
//                        (index == 0 && position % dotCount == dotCount - 1) ||
//                        (index == dotCount - 1 && position % dotCount == 0) -> mediumDotSize
//                else -> smallDotSize
            }.dpToPx()

            dot.updateLayoutParams<LayoutParams> {
                width = size
                height = size
            }
        }
        requestLayout()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}