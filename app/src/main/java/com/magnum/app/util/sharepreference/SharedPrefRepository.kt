package com.magnum.app.util.sharepreference

import androidx.fragment.app.FragmentActivity
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class SharedPrefRepository(
    activity: FragmentActivity
) {
    private var mSharedPrefManager: SharedPrefsManager = SharedPrefsManager.create(activity)
    private val disposables = CompositeDisposable()

    fun getToken(sharedPrefCallBack: SharedPrefCallBack) {
        disposables.add(mSharedPrefManager.tokenObserver()
            .doOnError {
                Timber.d("mSharedPrefManager $it")
                sharedPrefCallBack.onError(it)
            }
            .subscribe({
                Timber.d("mSharedPrefManager gotToken")

                sharedPrefCallBack.tokenCallBack(it)
            }, {
                Timber.d("mSharedPrefManager $it")
                sharedPrefCallBack.onError(it)
            })
        )
    }

    fun setToken(token: String) {
        disposables.add(mSharedPrefManager
            .saveToken(token)
            .doOnError { Timber.d("mSharedPrefManager $it") }
            .subscribe({
                Timber.d("mSharedPrefManager token Stored")
            }, {
                Timber.d("mSharedPrefManager $it")
            })
        )
    }

    fun clearDataBase() {
        disposables.add(mSharedPrefManager
            .clearPreferences()
            .doOnError { Timber.d("mSharedPrefManager $it") }
            .subscribe({
                Timber.d("mSharedPrefManager database cleared")

            }, {
                Timber.d("mSharedPrefManager $it")
            })
        )
    }

    fun destroyDisposables() {
        disposables.clear()
    }
}