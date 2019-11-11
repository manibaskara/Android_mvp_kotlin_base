package com.magnum.app.model.dto.common

import com.sendbird.android.shadow.com.google.gson.annotations.SerializedName


class FacebookData {

    val id: String = ""

    @SerializedName("first_name")
    val firstName: String = ""

    @SerializedName("last_name")
    val lastName: String = ""

    val email: String = ""

    val profilePic: String = "https://graph.facebook.com/$id/picture?height=500"
}

class PictureObject(
    var data: PictureData? = null
)


class PictureData(

    var height: String? = "",

    var url: String? = "",

    var width: String? = ""
)


/*
* {
"id":"105502950653362",
"name":"Gowtham Dev",
"picture":{
"data":{
"height":480,
"is_silhouette":true,
"url":"https:\/\/platform-lookaside.fbsbx.com\/platform\/profilepic\/?asid=105502950653362&height=480&width=480&ext=1558515716&hash=AeSYsnaFKKtz0MOn",
"width":480
}
}
}

*
*
* */