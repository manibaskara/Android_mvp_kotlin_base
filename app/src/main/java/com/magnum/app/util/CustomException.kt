package com.magnum.app.util

import com.magnum.app.model.dto.response.BaseResponse
import okhttp3.ResponseBody

class CustomException : Exception {

    var code: Int = 0

    var exception: String? = null

    constructor(code: Int, exception: String?) {
        this.code = code
        this.exception = exception
    }

    constructor(code: Int, throwable: Throwable?) {
        this.code = code
        this.exception = throwable?.message
    }

    constructor(code: Int, response: BaseResponse?) {
        this.code = code
        this.exception = response?.message
    }

    constructor(code: Int, response: ResponseBody?) {
        this.code = code
        this.exception = response.toString()
    }

    constructor()

}