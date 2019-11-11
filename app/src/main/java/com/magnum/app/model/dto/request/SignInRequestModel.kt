package com.magnum.app.model.dto.request

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class SignInRequestModel(
    @field:Json(name = "userName")
    val userName: String = "",

    @field:Json(name = "password")
    val password: String = "",

    @field:Json(name = "deviceToken")
    val deviceToken: String = ""

) : Parcelable