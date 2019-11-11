package com.magnum.app.socialnetwork

interface AuthenticationListener {
    fun onTokenReceived(auth_token: String, socialMediaType: String)
}
