package com.magnum.app.util.sharepreference


//override only needed functions to be observed
interface SharedPrefCallBack {

    fun tokenCallBack(token: String){}

    fun onError(throwable: Throwable){}

}