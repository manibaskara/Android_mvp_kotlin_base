package com.magnum.app.model.dto.response

import com.squareup.moshi.Json


data class ForceUpdateResponse(
    @field:Json(name = "data")
    var data: ForceUpdate = ForceUpdate()
) : BaseResponse()

data class ForceUpdate(
    @field:Json(name = "forceUpdate")
    var forceUpdate: Boolean? = false,
    @field:Json(name = "minimumVersion")
    var minimumVersion: String? = ""
)