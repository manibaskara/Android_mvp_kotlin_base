package com.magnum.app.view.iview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.magnum.app.adapter.listener.DefaultAlertListener
import com.magnum.app.util.CodeSnippet
import com.magnum.app.util.CustomException
import com.magnum.app.util.sharepreference.SharedPrefRepository
import com.magnum.app.view.widgets.exoplayer.ExoPlayerVideoHandler

interface IView {

    fun showMessage(message: String?)

    fun showMessage(message: Int)

    fun showMessage(message: CustomException)

    fun showMessage(throwable: Throwable)

    fun showSnackBar(message: String?)

    fun showProgressbar()

    fun dismissProgressbar()

    fun showNetworkMessage()

    fun getActivity(): FragmentActivity?

    fun hideKeyBoard() {}

    fun onClickBack()

    fun replaceFragment(layoutId: Int, fragment: Fragment, fragmentTag: String)

    fun replaceFragmentWithAnimation(
        layoutId: Int,
        fragment: Fragment,
        animationDirection: Int
    )

    fun showFullScreenDialog(dialog: DialogFragment, bundle: Bundle?)

    fun <G> navigateTo(className: Class<G>, bundle: Bundle?, needDefaultAnim: Boolean)

    fun startActivityWithIntent(intent: Intent)

    fun navigateToHome(bundle: Bundle?, needDefaultAnim: Boolean)

    fun replaceFragmentWithBackStack(
        layoutId: Int,
        fragment: Fragment,
        currentFragment: Fragment,
        fragmentCode: Int,
        fragmentTag: String,
        needDefaultAnim: Boolean
    )

    fun showKeyBoard(view: View)

    fun setFragment(fragment: Fragment, container: Int, fragmentTag: String)

    //   fun logoutUser()

    fun <G> navigateToWithResult(
        className: Class<G>,
        bundle: Bundle?,
        needDefaultAnim: Boolean,
        activityRequestCode: Int
    )

    fun showSnackBar(view: View, message: String)

    fun doubleClickExit()

    fun finishActivity()

    fun showDefaultAlerter(
        alertText: String, alertIcon: Int,
        alertListener: DefaultAlertListener
    )

    fun pauseVideo()

    fun getExoPlayer(): ExoPlayerVideoHandler?

    fun showForceUpdateDialog()

    fun showCustomDialog(dialogFragment: Fragment)

    val codeSnippet: CodeSnippet?

    val sharedPrefRepository: SharedPrefRepository?
}