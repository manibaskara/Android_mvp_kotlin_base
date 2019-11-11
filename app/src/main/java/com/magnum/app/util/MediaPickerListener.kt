package com.magnum.app.util

import android.net.Uri

interface MediaPickerListener {

    fun onSuccess(uriList: MutableList<Uri>)

    fun onError(error: String)

}