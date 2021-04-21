package com.lib.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lib.R

class ItemDecorator : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        with(outRect) {
            top = view.resources.getDimension(R.dimen.album_top_bottom_margin).toInt()
            left = view.resources.getDimension(R.dimen.album_left_right_margin).toInt()
            right = view.resources.getDimension(R.dimen.album_left_right_margin).toInt()
            if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1)) {
                bottom = view.resources.getDimension(R.dimen.album_top_bottom_margin).toInt()
            }
        }
    }
}
