package com.magnum.app.util.sharepreference

import android.content.Context
import android.content.SharedPreferences
import com.magnum.app.common.Constants.PreferenceKey.Companion.PROFILE_DATA
import com.magnum.app.common.Constants.PreferenceKey.Companion.SHARED_PREF_NAME
import com.magnum.app.common.Constants.PreferenceKey.Companion.TOKEN
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class SharedPrefsManager(preferences: SharedPreferences) : ISharedPrefsManager {

    private val prefSubject = BehaviorSubject.createDefault(preferences)

    private val prefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            prefSubject.onNext(sharedPreferences)
        }

    companion object {
        @JvmStatic
        fun create(context: Context): SharedPrefsManager {
            val preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return SharedPrefsManager(preferences)
        }
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    override fun saveToken(token: String): Completable =
        prefSubject.firstOrError().editSharedPreferences { putString(TOKEN, token) }

    override fun tokenObserver(): Observable<String> = prefSubject.map { it.getString(TOKEN, "") }

    override fun clearToken(): Completable = prefSubject.firstOrError()
        .clearSharedPreferences {
            remove(TOKEN)
        }

 /*   override fun saveUserData(userData: UserData): Completable = prefSubject
        .firstOrError()
        .editSharedPreferences {
            putString(PROFILE_DATA, userData.moshiObjToString(UserData::class.java))
        }

    override fun userDataObserver(): Observable<UserData> = prefSubject
        .map {
            it.getString(
                PROFILE_DATA,
                UserData().moshiObjToString(UserData::class.java)
            )?.moshiStringToObj(UserData::class.java)
        }*/

    override fun clearUserData(): Completable {
        return prefSubject.firstOrError()
            .clearSharedPreferences {
                remove(PROFILE_DATA)
            }
    }

    override fun clearPreferences(): Completable {
        return prefSubject.firstOrError()
            .clearSharedPreferences {
                remove(PROFILE_DATA)
                remove(TOKEN)
            }
    }
}

fun Single<SharedPreferences>.editSharedPreferences(batch: SharedPreferences.Editor.() -> Unit): Completable =
    flatMapCompletable {
        Completable.fromAction {
            it.edit().also(batch).apply()
        }
    }

fun Single<SharedPreferences>.clearSharedPreferences(batch: SharedPreferences.Editor.() -> Unit): Completable =
    flatMapCompletable {
        Completable.fromAction {
            it.edit().also(batch).apply()
        }
    }