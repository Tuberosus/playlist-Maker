package com.example.playlistmaker.ui.media.fragments.playlists

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class PaddingItemDecorator: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)

        val dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            view.resources.getDimension(R.dimen.setting_title_margin),
            view.resources.displayMetrics).toInt()

        outRect.left = 0
        outRect.right = 0
        outRect.top = 0
        outRect.bottom = 0
    }
}