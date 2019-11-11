package com.magnum.app.adapter

import android.view.ViewGroup
import com.magnum.app.R
import com.magnum.app.adapter.listener.BaseRecyclerAdapterListener
import com.magnum.app.adapter.viewHolder.GalleryViewHolder
import com.magnum.app.databinding.InflateGalaryImageBinding
import com.magnum.app.model.dto.common.GalleryImage

class GalleryAdapter(
    data: MutableList<GalleryImage>,
    var listener: BaseRecyclerAdapterListener<GalleryImage>
) : BaseRecyclerAdapter<GalleryImage, GalleryViewHolder>(data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            inflateDataBinding(
                R.layout.inflate_galary_image,
                parent
            ) as InflateGalaryImageBinding, listener
        )
    }
}