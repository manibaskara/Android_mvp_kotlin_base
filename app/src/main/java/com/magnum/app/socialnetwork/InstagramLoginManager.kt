package com.magnum.app.socialnetwork

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.fragment.app.FragmentActivity
import com.magnum.app.R
import com.magnum.app.common.Constants.RequestCodes.Companion.INSTAGRAM_SIGN_IN
import com.magnum.app.common.Constants.SocialMediaType.Companion.INSTAGRAM_INTEGER
import com.magnum.app.common.Constants.SocialMediaType.Companion.INSTAGRAM_NAME
import com.magnum.app.model.dto.request.SocialSignInRequest
import com.magnum.app.util.CodeSnippet
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException

class InstagramLoginManager : AuthenticationListener {

    private var codeSnippet: CodeSnippet? = null
    private lateinit var activity: FragmentActivity
    private lateinit var listener: OnInstaLoginListener

    interface OnInstaLoginListener {
        fun onSuccess(socialSignInRequest: SocialSignInRequest)
        fun onFailed(exception: Exception)
    }

    fun init(fragmentActivity: FragmentActivity, onInstaLoginListener: OnInstaLoginListener) {
        activity = fragmentActivity
        listener = onInstaLoginListener
        codeSnippet = CodeSnippet(activity)

        val request_url = codeSnippet?.getString(R.string.instagram_base_url) +
                "oauth/authorize/?client_id=" +
                codeSnippet?.getString(R.string.instagram_client_id) +
                "&redirect_uri=" + codeSnippet?.getString(R.string.instagram_redirect_url) +
                "&response_type=token&display=touch&scope=basic"

        val uri = Uri.parse(request_url)
        val likeIng = Intent(Intent.ACTION_PICK, uri)

        likeIng.setPackage("com.instagram.android")

        try {
            activity.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            //Log.e("onClickInstagram", "" + e.cause)
            val intent = Intent(activity, InstaAuthDialog::class.java)
                .putExtra("SocialMediaType", INSTAGRAM_NAME)
            activity.startActivityForResult(intent, INSTAGRAM_SIGN_IN)
            /*val auth_dialog = InstaAuthDialog(this)
            auth_dialog.setInstragramUrlDetails(this,this)
            auth_dialog.setCancelable(true)
            auth_dialog.show()*/
        }
    }

    override fun onTokenReceived(auth_token: String, socialMediaType: String) {
        if (socialMediaType == INSTAGRAM_NAME)
            getUserInfoByAccessToken(auth_token)

    }

    /**
     * method name : onActivityResult
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent
     * description : This method is used to pass the onActivity result to facebook callbackmanager
     * */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == INSTAGRAM_SIGN_IN) {
            //Toast.makeText(activity, "onSuccess $data", Toast.LENGTH_LONG).show()
            if (data != null) {
                val code = data.getStringExtra("code")
                val socialMediaType = data.getStringExtra("SocialMediaType")
                code?.let {
                    if (socialMediaType != null)
                        onTokenReceived(it, socialMediaType)
                }
            }
        }
    }

    private fun getUserInfoByAccessToken(token: String) {
        RequestInstagramAPI(token).execute()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class RequestInstagramAPI(var token: String) : AsyncTask<Void, String, String>() {
        override fun doInBackground(vararg params: Void): String? {
            val httpClient = DefaultHttpClient()
            try {
                // mediaLink is something like "https://instagram.com/p/6GgFE9JKzm/" or
                // "https://instagram.com/_u/sembozdemir"
                val uri =
                    Uri.parse(codeSnippet?.getString(R.string.instagram_get_user_info_url) + token)
                val intent = Intent(Intent.ACTION_VIEW, uri)

                intent.setPackage("com.instagram.android")
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Timber.e("RequestInstagramAPI $e.message")

                val httpGet =
                    HttpGet(codeSnippet?.getString(R.string.instagram_get_user_info_url) + token)

                try {
                    val response = httpClient.execute(httpGet)
                    val httpEntity = response.entity
                    return EntityUtils.toString(httpEntity)
                } catch (e1: IOException) {
                    listener.onFailed(e)
                    e1.printStackTrace()
                }

            }

            return null
        }

        override fun onPostExecute(response: String?) {
            super.onPostExecute(response)
            if (response != null) {
                try {
                    val jsonObject = JSONObject(response)
                    Timber.e("response $jsonObject")
                    val jsonData = jsonObject.getJSONObject("data")
                    if (jsonData.has("id")) {

                        val socialSignInRequest = SocialSignInRequest()
                        socialSignInRequest.socialId = jsonData.getString("id")
                        socialSignInRequest.name = jsonData.getString("username")
                        socialSignInRequest.socialType = INSTAGRAM_INTEGER
                        //socialSignInRequest.profileUrl = jsonData.getString("profile_picture")

                        listener.onSuccess(socialSignInRequest)

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    listener.onFailed(e)
                }

            } else {
                listener.onFailed(exception = object : java.lang.Exception() {
                    override val message: String?
                        get() = "unable to get profile"
                })
            }
        }
    }

    companion object {
        val INSTANCE: InstagramLoginManager by lazy {
            InstagramLoginManager()
        }
    }
}