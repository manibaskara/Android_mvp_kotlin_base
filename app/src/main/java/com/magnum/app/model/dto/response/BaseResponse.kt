package com.magnum.app.model.dto.response

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseResponse(

    @field:Json(name = "message")
    var message: String? = "",

    @field:Json(name = "status")
    var status: Int? = 0,

    @field:Json(name = "err")
    var error: String = ""

) : Parcelable