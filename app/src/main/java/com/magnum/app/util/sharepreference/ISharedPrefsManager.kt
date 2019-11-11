package com.magnum.app.util.sharepreference

import io.reactivex.Completable
import io.reactivex.Observable

interface ISharedPrefsManager {

    fun saveToken(token: String): Completable

    fun tokenObserver(): Observable<String>

    fun clearToken(): Completable

    fun clearUserData(): Completable

    fun clearPreferences(): Completable

}