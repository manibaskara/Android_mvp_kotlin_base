package com.magnum.app.adapter.listener

interface ConfirmDialogListener {

    fun onClickPositive()

    //override if need to take any action on cancel
    fun onClickCancel() {}
}