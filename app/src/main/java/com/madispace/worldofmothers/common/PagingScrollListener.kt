package com.madispace.worldofmothers.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagingScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val isLoading: () -> Boolean,
    private val runLoadingBlock: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

            if (!isLoading.invoke()) {
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    runLoadingBlock.invoke()
                }
            }
        }
    }
}