package com.magnum.app.adapter.viewHolder

import com.magnum.app.adapter.listener.BaseRecyclerAdapterListener
import com.magnum.app.databinding.InflateGalaryImageBinding
import com.magnum.app.model.dto.common.GalleryImage

class GalleryViewHolder(
    view: InflateGalaryImageBinding,
    var listener: BaseRecyclerAdapterListener<GalleryImage>
) : BaseViewHolder<GalleryImage, InflateGalaryImageBinding>(view) {

    override fun populateData(data: GalleryImage) {
        if (data.photo?.isNotEmpty() == true)
            codeSnippet.getGlide().load(data.photo)
                .into(viewDataBinding.ivModelImage)
    }
}