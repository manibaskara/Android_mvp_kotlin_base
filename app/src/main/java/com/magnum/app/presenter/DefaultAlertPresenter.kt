package com.magnum.app.presenter

import android.os.Bundle
import com.magnum.app.presenter.ipresenter.IPresenter
import com.magnum.app.view.iview.IView

class DefaultAlertPresenter(view: IView) : BasePresenter<IView>(view),
    IPresenter {
    override fun onCreate(bundle: Bundle?) {

    }
}