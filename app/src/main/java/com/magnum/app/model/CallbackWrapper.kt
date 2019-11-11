package com.magnum.app.model

import com.magnum.app.common.Constants.HttpErrorMessage.Companion.FORBIDDEN
import com.magnum.app.common.Constants.HttpErrorMessage.Companion.INTERNAL_SERVER_ERROR
import com.magnum.app.common.Constants.HttpErrorMessage.Companion.TIMEOUT
import com.magnum.app.common.Constants.HttpErrorMessage.Companion.UNAUTHORIZED
import com.magnum.app.common.Constants.InternalHttpCode.Companion.BAD_REQUEST_CODE
import com.magnum.app.common.Constants.InternalHttpCode.Companion.FAILURE_CODE
import com.magnum.app.common.Constants.InternalHttpCode.Companion.INTERNAL_SERVER_ERROR_CODE
import com.magnum.app.common.Constants.InternalHttpCode.Companion.TIMEOUT_CODE
import com.magnum.app.common.Constants.InternalHttpCode.Companion.UNAUTHORIZED_CODE
import com.magnum.app.model.dto.response.BaseResponse
import com.magnum.app.model.dto.response.ErrorResponse
import com.magnum.app.model.webservice.ApiClient
import com.magnum.app.util.CustomException
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException

abstract class CallbackWrapper<T : BaseResponse>(private var apiClient: ApiClient) :
    DisposableObserver<T>() {

    private val tag = javaClass.simpleName

    override fun onComplete() {

    }

    override fun onNext(t: T) {
        success(t)
    }

    override fun onError(e: Throwable) {
        Timber.d("onError: $e")
        when (e) {
            is HttpException -> when {
                e.code() == UNAUTHORIZED_CODE -> failure(
                    CustomException(
                        UNAUTHORIZED_CODE,
                        UNAUTHORIZED
                    )
                )
                e.code() == INTERNAL_SERVER_ERROR_CODE -> failure(
                    CustomException(
                        INTERNAL_SERVER_ERROR_CODE,
                        INTERNAL_SERVER_ERROR
                    )
                )
                e.code() == FAILURE_CODE -> failure(parseError(e))
            }
            is SocketTimeoutException -> failure(CustomException(TIMEOUT_CODE, TIMEOUT))
            is IOException -> failure(CustomException(BAD_REQUEST_CODE, FORBIDDEN))
            else -> failure(CustomException(INTERNAL_SERVER_ERROR_CODE, INTERNAL_SERVER_ERROR))
        }
    }

    private fun parseError(throwable: HttpException?): CustomException {
        val converter: Converter<ResponseBody, ErrorResponse> =
            apiClient.getClient().responseBodyConverter(ErrorResponse::class.java, emptyArray())
        return CustomException(
            throwable?.response()?.code() ?: 500,
            throwable?.response()?.errorBody()?.let { converter.convert(it).message }
        )
    }

    abstract fun success(response: T)

    abstract fun failure(e: Throwable)
}