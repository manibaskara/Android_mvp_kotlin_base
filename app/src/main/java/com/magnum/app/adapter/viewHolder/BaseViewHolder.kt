package com.magnum.app.adapter.viewHolder

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.magnum.app.util.CodeSnippet

abstract class BaseViewHolder<T, VB : ViewDataBinding> : RecyclerView.ViewHolder {

    internal var codeSnippet = CodeSnippet(itemView.context)

    var data: T? = null
        set(value) {
            field = value
            data?.let { populateData(it) }
        }

    protected lateinit var viewDataBinding: VB

    open var lastItemPosition = 0

    open var listLength =
        if (data is MutableList<*> && !(data as MutableList<*>).isNullOrEmpty()) (data as MutableList<*>).size - 1 else 0

    internal constructor(viewDataBinding: VB) : super(viewDataBinding.root) {
        this.viewDataBinding = viewDataBinding
    }

    internal constructor(itemView: View) : super(itemView)

    internal abstract fun populateData(data: T)

}