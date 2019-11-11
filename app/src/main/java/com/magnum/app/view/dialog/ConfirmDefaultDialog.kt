package com.magnum.app.view.dialog

import com.magnum.app.R
import com.magnum.app.adapter.listener.ConfirmDialogListener
import com.magnum.app.databinding.InflateConfirmDialogBinding
import com.magnum.app.model.dto.common.ConfirmAlertData
import com.magnum.app.presenter.DefaultAlertPresenter
import com.magnum.app.presenter.ipresenter.IPresenter
import com.magnum.app.view.iview.IView

class ConfirmDefaultDialog(
    private val data: ConfirmAlertData,
    private val listener: ConfirmDialogListener
) : BaseDialogFragment<IPresenter, InflateConfirmDialogBinding>(),
    IView {

    override fun getLayoutRes(): Int {
        return R.layout.inflate_confirm_dialog
    }

    override fun onInitializePresenter(): IPresenter {
        return DefaultAlertPresenter(this)
    }

    override fun onPresenterInitialized() {
        super.onPresenterInitialized()
        viewDataBinding?.data = data
        isCancelable = true
        viewDataBinding?.listener = listener
        viewDataBinding?.btnCancel?.setOnClickListener {
            listener.onClickCancel()
            dismissDialog()
        }

        viewDataBinding?.btnShare?.setOnClickListener {
            listener.onClickPositive()
            dismissDialog()
        }
    }
}