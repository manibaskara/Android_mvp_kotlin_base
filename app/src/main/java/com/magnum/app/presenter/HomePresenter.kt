package com.magnum.app.presenter

import android.os.Bundle
import com.magnum.app.presenter.ipresenter.IHomePresenter
import com.magnum.app.util.sharepreference.SharedPrefCallBack
import com.magnum.app.view.iview.IHomeView

class HomePresenter(iHomeView: IHomeView) : BasePresenter<IHomeView>(iHomeView), IHomePresenter,
    SharedPrefCallBack {

    override fun onCreate(bundle: Bundle?) {
    }

}