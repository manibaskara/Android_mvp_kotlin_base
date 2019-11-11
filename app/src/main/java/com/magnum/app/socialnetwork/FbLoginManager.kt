package com.magnum.app.socialnetwork

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.magnum.app.common.Constants
import com.magnum.app.model.dto.common.FacebookData
import com.magnum.app.model.dto.request.SocialSignInRequest
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.sendbird.android.shadow.com.google.gson.Gson
import timber.log.Timber


class FbLoginManager {
    private var callbackManager: CallbackManager? = null

    /**
     * This listener used to give call-back to UI.
     * onSuccess it will return socialSignInRequest
     * onFailed it will return fb exception
     * */
    interface FbLoginListener {
        fun onSuccess(socialSignInRequest: SocialSignInRequest)
        fun onFailed(exception: FacebookException)
    }

    /**
     * method name : init
     * @param activity FragmentActivity
     * @param listener FbLoginListener
     * description : This method is used to initialize fb login manager
     * */
    fun init(activity: FragmentActivity, listener: FbLoginListener) {

        FacebookSdk.sdkInitialize(getApplicationContext())
          LoginManager.getInstance().loginBehavior = LoginBehavior.WEB_VIEW_ONLY
        //  LoginManager.getInstance().logOut()
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .logInWithReadPermissions(
                activity,
                listOf(
                    Constants.FaceBookKeys.USER_EMAIL,
                    Constants.FaceBookKeys.USER_PUBLIC_PROFILE
                )
            )

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val dataRequest =
                        GraphRequest.newMeRequest(loginResult.accessToken) { json_object, _ ->

                            Timber.d(json_object.toString())
                            Timber.d(json_object.toString())
                            val facebookData =
                                Gson().fromJson(json_object.toString(), FacebookData::class.java)

                            val socialSignInRequest = SocialSignInRequest(
                                name = facebookData.firstName + " " + facebookData.lastName,
                                socialType = 1,
                                socialId = facebookData.id,
                                email = facebookData.email
                            )
                            listener.onSuccess(socialSignInRequest)
                            LoginManager.getInstance().logOut()
                        }
                    val params = Bundle()
                    params.putString(
                        Constants.FaceBookKeys.FB_FIELDS,
                        Constants.FaceBookKeys.FB_FIELDS_KEYS
                    )
                    //params.putString("fields", "id,name,email,link,picture.width(120).height
                    // (120)")
                    dataRequest.parameters = params
                    dataRequest.executeAsync()
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    listener.onFailed(exception)
                }
            })
    }

    /**
     * method name : onActivityResult
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent
     * description : This method is used to pass the onActivity result to facebook callbackManager
     * */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (callbackManager != null)
            callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun logOutFacebook() {
        LoginManager.getInstance()?.logOut()
        AccessToken.setCurrentAccessToken(null)

    }

    companion object {
        val INSTANCE: FbLoginManager by lazy {
            FbLoginManager()
        }
    }
}