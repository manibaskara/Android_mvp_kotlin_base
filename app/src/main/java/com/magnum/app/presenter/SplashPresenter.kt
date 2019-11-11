package com.magnum.app.presenter

import android.os.Bundle
import com.magnum.app.presenter.ipresenter.ISplashPresenter
import com.magnum.app.util.sharepreference.SharedPrefCallBack
import com.magnum.app.view.iview.ISplashView
import timber.log.Timber

class SplashPresenter(iSplashView: ISplashView) : BasePresenter<ISplashView>(iSplashView),
    ISplashPresenter, SharedPrefCallBack {

    private var token = ""

    override fun onCreate(bundle: Bundle?) {
        Timber.d("bundleItem  ${bundle?.toString()}")
        sharedRepository?.getToken(this)

    }

    override fun tokenCallBack(token: String) {
        this.token = token

    }

    override fun checkLoggedInStatus() {
        if (token.isNotEmpty()) {
            Timber.e("Token = $token")
            getProfile()
        } else {
            navigateToSignUp()
        }
    }

    override fun getProfile() {
        //get userData
        //sharedRepository?.setUserData(userData)
        iView?.navigateToHome(null, true)
        iView?.finishActivity()
    }

    private fun navigateToSignUp() {
        iView?.navigateToHome(null, true)
        iView?.finishActivity()
    }

    override fun checkForceUpdate() {

        if (false) {
            iView?.showForceUpdateDialog()
        } else {
            checkLoggedInStatus()
        }
    }
}