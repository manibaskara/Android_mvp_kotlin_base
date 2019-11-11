package com.magnum.app.model.dto.common

import com.magnum.app.R

data class DefaultAlertData(
    var alertIcon: Int = R.drawable.ic_tick_green,
    var message: String? = ""
)