package com.magnum.app.adapter.listener


interface BaseRecyclerAdapterListener<T> {

    fun onClickItem(position: Int, data: T?)

}