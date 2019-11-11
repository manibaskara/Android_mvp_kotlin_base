package com.magnum.app.presenter.ipresenter

import com.magnum.app.util.CodeSnippet
import com.magnum.app.util.CustomException
import com.magnum.app.util.sharepreference.SharedPrefRepository
interface IRepositoryModel {

    fun isInternetConnected(): Boolean?

    fun getCodeSnippet(): CodeSnippet?

    fun showNetworkUnavailable()

    fun showProgressbar()

    fun dismissProgressbar()

    fun logOutUser()

    fun updateUserProfileInLocal()

    fun showMessage(message: String?)

    fun showMessage(message: CustomException)

    fun showMessage(throwable: Throwable)

    val sharedRepository: SharedPrefRepository?
}