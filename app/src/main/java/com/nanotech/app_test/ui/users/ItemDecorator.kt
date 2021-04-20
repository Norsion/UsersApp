package com.nanotech.app_test.ui.users

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(
    private val space: Int = 0,
    divider: Drawable? = null
) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = null

    init {
        this.mDivider = divider
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom: Int = dividerTop + (mDivider?.intrinsicHeight ?: 2)
            val dividerLeft: Int = child.left + space
            mDivider?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider?.draw(c)
        }
    }
}