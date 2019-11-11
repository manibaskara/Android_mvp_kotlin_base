package com.magnum.app.model.dto.common

import com.squareup.moshi.Json

data class GalleryImage(
    @field:Json(name = "_id")
    var id: String? = "",
    @field:Json(name = "photo")
    var photo: String? = ""
)