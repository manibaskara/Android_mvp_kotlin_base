package com.magnum.app.model

import android.content.Context
import com.google.gson.Gson
import com.magnum.app.common.Constants.InternalHttpCode.Companion.UNAUTHORIZED_CODE
import com.magnum.app.model.dto.response.BaseResponse
import com.magnum.app.model.dto.response.ForceUpdateResponse
import com.magnum.app.model.webservice.ApiClient
import com.magnum.app.presenter.ipresenter.IRepositoryModel
import com.magnum.app.util.CustomException
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

open class ModelRepository(context: Context, private var iRepositoryModel: IRepositoryModel) {

    private var apiClient = ApiClient(context)

    private fun <T : BaseResponse> enqueue(callback: Call<T>): Observable<T> {
        if (iRepositoryModel.isInternetConnected() == true) {
            showProgressbar()
            @Suppress("RemoveExplicitTypeArguments")
            return Observable.create<T> { emitter ->
                callback.enqueue(object : Callback<T> {

                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        if (response.body() != null && response.isSuccessful) {
                            val result = response.body()
                            if (response.code() == 200) {
                                dismissProcess()
                                result?.let { it1 -> emitter.onNext(it1) }
                            } else {
                                dismissProcess()
                                emitter.onError(
                                    CustomException(
                                        response.code(),
                                        result
                                    )
                                )
                            }
                        } else if (response.body() != null) {
                            dismissProcess()
                            emitter.onError(CustomException(response.code(), response.body()))
                        } else if (response.code() == UNAUTHORIZED_CODE) {
                            dismissProcess()
                            emitter.onError(
                                CustomException(
                                    response.code(),
                                    Gson().fromJson(
                                        response.errorBody()?.charStream(),
                                        BaseResponse::class.java
                                    ).message as String
                                )
                            )
                            if (response.code() == UNAUTHORIZED_CODE)
                                logoutUser()
                        } else {
                            dismissProcess()
                            try {
                                emitter.onError(
                                    CustomException(
                                        response.code(),
                                        Gson().fromJson(
                                            response.errorBody()?.charStream(),
                                            BaseResponse::class.java
                                        ).message as String
                                    )
                                )
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<T>, t: Throwable) {
                        try {
                            dismissProcess()
                            Timber.d("onFailure : $t")
                            t.let { _ ->
                                emitter.onError(
                                    CustomException(
                                        501,
                                        t.localizedMessage
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            t.let { _ -> emitter.onError(CustomException(502, e.toString())) }
                            dismissProcess()
                        }
                    }
                })
            }
        } else {
            iRepositoryModel.showNetworkUnavailable()
            dismissProcess()
            return Observable.never()
        }
    }

    private fun dismissProcess() {
        iRepositoryModel.dismissProgressbar()
    }

    private fun logoutUser() {
        iRepositoryModel.logOutUser()
    }

    private fun showProgressbar() {
        iRepositoryModel.showProgressbar()
    }


    fun getForceUpdate(): Observable<ForceUpdateResponse> {
        return enqueue(apiClient.getApiInterface().getForceUpdate())
    }

}