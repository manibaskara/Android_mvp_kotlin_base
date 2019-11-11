package com.magnum.app.view.activity

import com.magnum.app.R
import com.magnum.app.databinding.ActivityHomeBinding
import com.magnum.app.presenter.HomePresenter
import com.magnum.app.presenter.ipresenter.IHomePresenter
import com.magnum.app.view.iview.IHomeView

class HomeActivity : BaseActivity<IHomePresenter, ActivityHomeBinding>(), IHomeView {

    override fun getLayoutRes(): Int {
        return R.layout.activity_home
    }

    override fun onInitializePresenter(): IHomePresenter {
        return HomePresenter(this)
    }

    override fun onPresenterInitialized() {
        viewDataBinding?.iView = this
    }
}