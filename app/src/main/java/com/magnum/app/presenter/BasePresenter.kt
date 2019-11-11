package com.magnum.app.presenter

import android.content.Intent
import com.magnum.app.presenter.ipresenter.IPresenter
import com.magnum.app.util.CodeSnippet
import com.magnum.app.util.CustomException
import com.magnum.app.util.sharepreference.SharedPrefRepository
import com.magnum.app.view.activity.SplashActivity
import com.magnum.app.view.iview.IView
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : IView>(protected var iView: T?) : IPresenter {

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
    }

    override fun onRestart() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    override fun isInternetConnected(): Boolean? {
        return getCodeSnippet()?.hasNetworkConnection()
    }

    override fun getCodeSnippet(): CodeSnippet? {
        return iView?.codeSnippet
    }

    override fun showNetworkUnavailable() {
        iView?.showNetworkMessage()
    }

    override fun showProgressbar() {
        iView?.showProgressbar()
    }

    override fun dismissProgressbar() {
        iView?.dismissProgressbar()
    }

    override fun logOutUser() {
        //  sharedRepository?.getUserData(sharedPrefCallBack)
        getCodeSnippet()?.resetFirebaseInstanceId()
        val intent = Intent(iView?.getActivity(), SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        iView?.startActivityWithIntent(intent)
        iView?.finishActivity()
        sharedRepository?.clearDataBase()
    }


    override fun showMessage(message: String?) {
        iView?.showMessage(message)
    }

    override fun showMessage(message: CustomException) {
        iView?.showMessage(message)
    }

    override fun showMessage(throwable: Throwable) {
        if (throwable is CustomException) {
            iView?.showMessage(throwable)
        }
    }

    override fun updateUserProfileInLocal() {
        val disposables = CompositeDisposable()
        // sharedRepository?.setUserData(data)
        disposables.clear()
    }

    override val sharedRepository: SharedPrefRepository?
        get() = iView?.sharedPrefRepository
}