package com.magnum.app.view.dialog

import android.content.DialogInterface
import com.magnum.app.R
import com.magnum.app.adapter.listener.DefaultAlertListener
import com.magnum.app.databinding.DefaultAlerterBinding
import com.magnum.app.model.dto.common.DefaultAlertData
import com.magnum.app.presenter.DefaultAlertPresenter
import com.magnum.app.presenter.ipresenter.IPresenter
import com.magnum.app.view.iview.IView

class DefaultAlertFragment(
    private val data: DefaultAlertData,
    private val listener: DefaultAlertListener
) : BaseDialogFragment<IPresenter, DefaultAlerterBinding>(),
    IView {

    override fun onCancel(dialog: DialogInterface) {
        listener.onCancel()
        super.onCancel(dialog)
    }

    override fun getLayoutRes(): Int {
        return R.layout.default_alerter
    }

    override fun onInitializePresenter(): IPresenter {
        return DefaultAlertPresenter(this)
    }

    override fun onPresenterInitialized() {
        super.onPresenterInitialized()
        viewDataBinding?.data = data
        viewDataBinding?.listener = listener
        viewDataBinding?.ivIcon?.setBackgroundResource(data.alertIcon)
        viewDataBinding?.cvDialog?.setOnClickListener {
            dismissDialog()
            listener.onCancel()
        }
    }
}