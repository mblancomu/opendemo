package com.manuelblanco.opendemo.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CustomGridRecyclerView(context: Context) : RecyclerView(context) {
    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout is GridLayoutManager) {
            super.setLayoutManager(layout)
        } else {
            throw ClassCastException("This recyclerview should use grid layout manager as the layout manager")
        }
    }

    override fun attachLayoutAnimationParameters(
        child: View?,
        params: ViewGroup.LayoutParams,
        index: Int,
        count: Int
    ) {
        if (adapter != null && layoutManager is GridLayoutManager) {
            val animationParams =
                params.layoutAnimationParameters as GridLayoutAnimationController.AnimationParameters
            val columns: Int = (layoutManager as GridLayoutManager).spanCount
            animationParams.count = count
            animationParams.index = index
            animationParams.columnsCount = columns
            animationParams.rowsCount = count / columns
            val invertedIndex = count - 1 - index
            animationParams.column = columns - 1 - invertedIndex % columns
            animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }
}

