package com.example.lab23

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class Decoration(
    private val resources: Resources
) : RecyclerView.ItemDecoration() {

    private val topBottomOffset: Int = 20.px()
    private val innerOffset: Int = 16.px()
    private val halfInnerOffset: Int = innerOffset / 2

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        outRect.top = when (position) {
            0 -> topBottomOffset
            1 -> topBottomOffset
            2 -> topBottomOffset
            else -> halfInnerOffset
        }

        outRect.bottom = if (position == itemCount - 1 || position == itemCount - 2) {
            topBottomOffset
        } else {
            halfInnerOffset
        }

        if (position % 2 == 0) {
            outRect.left = innerOffset
            outRect.right = halfInnerOffset
        }
        if (position == 0) {
            outRect.left = halfInnerOffset
            outRect.right = halfInnerOffset
        }
        else {
            outRect.left = halfInnerOffset
            outRect.right = innerOffset
        }
    }

    private fun Int.px(): Int {
        val floatPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        )
        return floatPx.toInt()
    }
}