package com.example.attempt

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EqualSpacing(private val spacing: Int, private val includeEdge: Boolean = true) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % 2 // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / 2
            outRect.right = (column + 1) * spacing / 2

            if (position < 2) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / 2
            outRect.right = spacing - (column + 1) * spacing / 2
            if (position >= 2) {
                outRect.top = spacing // item top
            }
        }
    }
}