package com.magnum.app.view.widgets.recylerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView : RecyclerView {
    private var emptyView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private val emptyObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkNullable(adapter)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkNullable(adapter)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkNullable(adapter)
        }
    }

    private fun checkNullable(adapter: Adapter<*>?) {
        if (adapter != null && emptyView != null) {
            if (adapter.itemCount == 0) {
                emptyView?.visibility = View.VISIBLE
                this@CustomRecyclerView.visibility = View.GONE
            } else {
                emptyView?.visibility = View.GONE
                this@CustomRecyclerView.visibility = View.VISIBLE
            }
        }
    }


    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }


    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
    }

    fun removeObserver() {
        adapter?.unregisterAdapterDataObserver(emptyObserver)
    }

}