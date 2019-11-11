package com.magnum.app.socialnetwork

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.magnum.app.R
import com.magnum.app.common.Constants.SocialMediaType.Companion.INSTAGRAM_NAME
import timber.log.Timber

class InstaAuthDialog : Activity() {
    private var requestUrl: String? = null
    private var redirectUrl: String? = null
    private var socialMediaType: String? = null

    private val webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return url.startsWith(redirectUrl!!)
        }

        override fun onPageFinished(view: WebView, url: String?) {
            super.onPageFinished(view, url)
            Timber.d("url $url")
            if (url?.contains("access_token=") == true || url?.contains("code=") == true) {
                var accessToken: String? = ""
                when (socialMediaType) {

                    INSTAGRAM_NAME -> {
                        val uri = Uri.parse(url)
                        accessToken = uri.encodedFragment
                        accessToken = accessToken?.substring(accessToken.lastIndexOf("=") + 1)
                    }
                }

                //if user tries login in to instagram with facebook, token will be returned as "_"
                if (accessToken?.isNotEmpty() == true && accessToken != "_") {
                    Timber.d("access_token $accessToken")
                    val returnIntent = Intent()
                    returnIntent.putExtra("code", accessToken)
                    returnIntent.putExtra("SocialMediaType", INSTAGRAM_NAME)

                    setResult(RESULT_OK, returnIntent)
                    /*listener.onTokenReceived(access_token,socialMediaType.name());*/
                    /*dismiss();*/

                    finish()
                }
            } else if (url?.contains("?error") == true) {
                Timber.d("access_token getting error fetching access token")
                /*dismiss();*/
                finish()
            } else if (url == requestUrl) {
                /*webView.reload();
                webView.setWebViewClient(webViewClient);
                repeat++;*/
                Toast.makeText(
                    this@InstaAuthDialog,
                    "Oops! Unable to get your details try again.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                finish()
            }
        }
    }

    private fun setInstragramUrlDetails(context: Context) {
        this.redirectUrl = context.resources.getString(R.string.instagram_redirect_url)
        this.requestUrl = context.resources.getString(R.string.instagram_base_url) +
                "oauth/authorize/?client_id=" +
                context.resources.getString(R.string.instagram_client_id) +
                "&redirect_uri=" + redirectUrl +
                "&response_type=token&display=touch&scope=basic"
        this.socialMediaType = INSTAGRAM_NAME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.instagram_auth_dialog)
        val data = intent
        if (data != null) {
            val socialMediaType = data.getStringExtra("SocialMediaType")
            if (socialMediaType == INSTAGRAM_NAME) {
                setInstragramUrlDetails(this)
            }
            Timber.d("OnCreate $socialMediaType")
        }
        initializeWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        /*webView.settings.domStorageEnabled = true
        webView.settings.savePassword = false
        webView.settings.saveFormData = false*/

        webView.loadUrl(requestUrl)
        webView.webViewClient = webViewClient
    }

}
