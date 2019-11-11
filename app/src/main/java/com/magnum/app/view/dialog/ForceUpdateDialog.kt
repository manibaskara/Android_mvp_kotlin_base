package com.magnum.app.view.dialog

import com.magnum.app.R
import com.magnum.app.adapter.listener.ForceUpdateListener
import com.magnum.app.databinding.InflateForceUpdateDialogBinding
import com.magnum.app.presenter.DefaultAlertPresenter
import com.magnum.app.presenter.ipresenter.IPresenter
import com.magnum.app.view.iview.IView

class ForceUpdateDialog(
    private val listener: ForceUpdateListener
) : BaseDialogFragment<IPresenter, InflateForceUpdateDialogBinding>(),
    IView {

    override fun getLayoutRes(): Int {
        return R.layout.inflate_force_update_dialog
    }

    override fun onInitializePresenter(): IPresenter {
        return DefaultAlertPresenter(this)
    }

    override fun onPresenterInitialized() {
        super.onPresenterInitialized()
        isCancelable = false
        viewDataBinding?.listener = listener
    }
}
