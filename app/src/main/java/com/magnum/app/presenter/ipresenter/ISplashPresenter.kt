package com.magnum.app.presenter.ipresenter

interface ISplashPresenter : IPresenter {

    fun getProfile()

    fun checkLoggedInStatus()

    fun checkForceUpdate()
}