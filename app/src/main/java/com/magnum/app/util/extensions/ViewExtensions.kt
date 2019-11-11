package com.magnum.app.util.extensions

import androidx.recyclerview.widget.RecyclerView
import com.magnum.app.adapter.BaseRecyclerAdapter
import com.magnum.app.util.pagingmodule.EndlessRecyclerViewScrollListener

/**
 * Used for paging - endless scroll
 * If layoutManager is already set not required to set again
 *
 * ====> calling example <=====
 *
 * var recyclerview = <INSTANCE OF RECYCLERVIEW>
 *
 *     recyclerview.paging{ pageCount, totalItemCount ->
 *          // YOUR LOGIC HERE
 *     }
 * Eg:
 *  override fun setListToRecyclerview(dataList: MutableList<PlaylistData>) {
bViewDataFragmentBinding.rvSongList.paging( dataList ,
listAdapter = RecomendedAdapter(dataList, iPresenter?.getAdapterListener()),
load = {page, totalCount -> iPresenter?.loadData(iPresenter?.category?.id) })
}
 *
 */
fun <D, A : BaseRecyclerAdapter<D, *>> RecyclerView.paging(
    list: List<D>,
    layoutManager: RecyclerView.LayoutManager? = this.layoutManager,
    listAdapter: A,
    load: (Int, Int) -> Unit
): RecyclerView {
    this.layoutManager = layoutManager
    if (tag == null) {
        tag = "Adapter set"
        adapter = listAdapter
        if (layoutManager == null)
            throw NullPointerException("Recyclerview should have layout manager setup before calling onLoadMore")
        this.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                println("On Load More called page-->$page")
                load(page, totalItemsCount)
            }
        })
    } else {

        (this.adapter as BaseRecyclerAdapter<D, *>).data?.addAll(list)
        this@paging.adapter?.notifyDataSetChanged()
    }
    return this
}

/**
 * Resets recyclerview tag, so adapter is set fresh with new data from start.
 * Important: Never call after [paging] call in the same method where list is updated, you will always get a new adapter.
 */
fun RecyclerView.pagingReset() {
    tag = null
}