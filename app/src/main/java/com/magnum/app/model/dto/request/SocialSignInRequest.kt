package com.magnum.app.model.dto.request

import com.squareup.moshi.Json


data class SocialSignInRequest(

    @field:Json(name = "name")
    var name: String? = "",

    @field:Json(name = "socialType")
    var socialType: Int? = 0,

    @field:Json(name = "socialId")
    var socialId: String? = "",

    @field:Json(name = "email")
    var email: String? = "",

    @field:Json(name = "deviceToken")
    var deviceToken: String? = ""

    /*   @field:Json(name = "firstName")
       var firstName: String? = "",

       @field:Json(name = "lastName")
       var lastName: String? = "",

       @field:Json(name = "profilePhoto")
       var profilePhoto: String? = "",
   */

)