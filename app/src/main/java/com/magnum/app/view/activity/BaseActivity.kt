package com.magnum.app.view.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import com.magnum.app.R
import com.magnum.app.adapter.listener.DefaultAlertListener
import com.magnum.app.adapter.listener.ForceUpdateListener
import com.magnum.app.common.Constants.AnimationDirection.Companion.LEFT
import com.magnum.app.common.Constants.AnimationDirection.Companion.RIGHT
import com.magnum.app.common.Constants.CommonKeys.Companion.EXIT_TIMEOUT
import com.magnum.app.model.dto.common.DefaultAlertData
import com.magnum.app.presenter.ipresenter.IPresenter
import com.magnum.app.util.CodeSnippet
import com.magnum.app.util.CustomException
import com.magnum.app.util.rxpermissions.RxPermissions
import com.magnum.app.util.sharepreference.SharedPrefRepository
import com.magnum.app.view.dialog.BaseDialogFragment
import com.magnum.app.view.dialog.DefaultAlertFragment
import com.magnum.app.view.dialog.ForceUpdateDialog
import com.magnum.app.view.iview.IView
import com.magnum.app.view.widgets.CustomProgressBar
import com.magnum.app.view.widgets.exoplayer.ExoPlayerVideoHandler
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import java.util.*

abstract class BaseActivity<IP : IPresenter, VB : ViewDataBinding> : FragmentActivity(), IView,
    View.OnTouchListener {

    protected var iPresenter: IP? = null
    protected var viewDataBinding: VB? = null
    private var mCodeSnippet: CodeSnippet? = null
    private var mSharedPrefRepository: SharedPrefRepository? = null
    private var mParentView: View? = null
    abstract fun getLayoutRes(): Int
    abstract fun onInitializePresenter(): IP
    private var mCustomProgressbar: CustomProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutRes())
        iPresenter = onInitializePresenter()
        iPresenter?.onCreate(intent?.extras)
        onPresenterInitialized()
    }

    fun getRxPermissions(): RxPermissions {
        return RxPermissions(this)
    }

    open fun onPresenterInitialized() {
    }

    override fun onStart() {
        super.onStart()
        iPresenter?.onStart()
    }

    override fun onResume() {
        super.onResume()
        iPresenter?.onResume()
    }

    override fun onPause() {
        super.onPause()
        codeSnippet?.hideKeyboard(this)
        iPresenter?.onPause()
    }

    override fun onDestroy() {
        sharedPrefRepository?.destroyDisposables()
        super.onDestroy()
        iPresenter?.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        iPresenter?.onRestart()
    }


    private fun getProgressBar(): CustomProgressBar? {
        if (mCustomProgressbar == null) {
            runOnUiThread {
                mCustomProgressbar = CustomProgressBar(this)
            }
        }
        return mCustomProgressbar
    }

    override fun showProgressbar() {
        getProgressBar()?.show()
    }

    override fun dismissProgressbar() {
        try {
            runOnUiThread {
                if (getProgressBar() != null && getProgressBar()?.isShowing == true)
                    getProgressBar()?.dismissProgress()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun finishActivity() {
        finish()
    }

    override fun pauseVideo() {
        ExoPlayerVideoHandler.getInstance().pauseVideo()
    }

    override fun showNetworkMessage() {

        mParentView?.let { parentView ->
            val snackBar =
                make(
                    parentView,
                    resources.getString(R.string.message_no_network_found),
                    Snackbar.LENGTH_LONG
                )
            snackBar.setActionTextColor(Color.WHITE)
            snackBar.setAction(R.string.action_settings) { codeSnippet?.showNetworkSettings() }
            snackBar.show()
        }
    }

    override val sharedPrefRepository: SharedPrefRepository?
        get() {
            return if (mSharedPrefRepository == null) {
                mSharedPrefRepository = SharedPrefRepository(this)
                mSharedPrefRepository
            } else {
                mSharedPrefRepository
            }
        }


    override val codeSnippet: CodeSnippet?
        get() {
            if (mCodeSnippet == null) {
                mCodeSnippet = CodeSnippet(this)
                return mCodeSnippet
            }
            return if (mCodeSnippet is CodeSnippet)
                mCodeSnippet as CodeSnippet
            else
                null
        }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        mParentView = window.decorView.findViewById(android.R.id.content)
        return super.onCreateView(name, context, attrs)
    }

    override fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun replaceFragment(layoutId: Int, fragment: Fragment, fragmentTag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(layoutId, fragment, fragmentTag)
        fragmentTransaction.commit()
    }

    @Suppress("unused")
    inner class DetailsTransition : TransitionSet() {
        init {
            ordering = ORDERING_TOGETHER
            addTransition(ChangeBounds()).addTransition(ChangeTransform())
                .addTransition(ChangeImageTransform())
        }
    }

    override fun replaceFragmentWithAnimation(
        layoutId: Int,
        fragment: Fragment,
        animationDirection: Int
    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (animationDirection) {
            RIGHT -> fragmentTransaction.setCustomAnimations(
                R.anim.slide_from_left,
                R.anim.slide_out_right
            )

            LEFT -> fragmentTransaction.setCustomAnimations(
                R.anim.slide_from_right,
                R.anim.slide_out_left
            )
        }
        fragmentTransaction.replace(layoutId, fragment)
        fragmentTransaction.commit()
    }

    override fun replaceFragmentWithBackStack(
        layoutId: Int,
        fragment: Fragment,
        currentFragment: Fragment,
        fragmentCode: Int,
        fragmentTag: String,
        needDefaultAnim: Boolean

    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (needDefaultAnim)
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_from_right,
                R.anim.slide_out_left
            )
        fragment.setTargetFragment(currentFragment, fragmentCode)
        fragmentTransaction.addToBackStack(currentFragment.tag)
        fragmentTransaction.add(layoutId, fragment, fragmentTag)
        fragmentTransaction.commit()
    }

    override fun setFragment(fragment: Fragment, container: Int, fragmentTag: String) {
        val simpleName = fragment.javaClass.simpleName
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val currentFragment = fragmentManager.primaryNavigationFragment
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment)
        }

        var fragmentTemp = fragmentManager.findFragmentByTag(simpleName)
        if (fragmentTemp == null) {
            fragmentTemp = fragment
            fragmentTransaction.add(container, fragmentTemp, fragmentTag)
        } else {
            fragmentTransaction.show(fragmentTemp)
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commit()
    }

    fun initHomeFragments(fragment: Fragment, container: Int, fragmentTag: String) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(container, fragment, fragmentTag)

    }

    override fun showMessage(message: Int) {
        showMessage(message.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        iPresenter?.onActivityResult(requestCode, resultCode, data)
    }

    override fun <G> navigateTo(className: Class<G>, bundle: Bundle?, needDefaultAnim: Boolean) {
        val intent = Intent(this, className)
        if (bundle != null)
            intent.putExtras(bundle)
        startActivity(intent)
        if (needDefaultAnim)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_out_left)
        else
            overridePendingTransition(0, 0)
    }

    override fun startActivityWithIntent(intent: Intent) {
        startActivity(intent)
    }

    override fun navigateToHome(bundle: Bundle?, needDefaultAnim: Boolean) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        if (bundle != null)
            intent.putExtras(bundle)
        startActivity(intent)
        if (needDefaultAnim)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_out_left)
    }

    override fun showKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun <G> navigateToWithResult(
        className: Class<G>,
        bundle: Bundle?,
        needDefaultAnim: Boolean,
        activityRequestCode: Int
    ) {
        val intent = Intent(this, className)
        if (bundle != null)
            intent.putExtras(bundle)
        startActivityForResult(intent, activityRequestCode)
        if (needDefaultAnim)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_out_left)
    }

    override fun getActivity(): FragmentActivity? {
        return this
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val returnValue: Boolean?
        returnValue = try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            true
        } catch (e: java.lang.Exception) {
            false
        }
        return returnValue
    }

    override fun hideKeyBoard() {
        codeSnippet?.hideKeyboard(this)
        //view.setOnTouchListener(this)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        input?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        return false
    }

    override fun onClickBack() {
        onBackPressed()
    }

    override fun showMessage(message: CustomException) {
        showMessage(message.exception ?: "Exception")
    }

    override fun showMessage(throwable: Throwable) {
        if (throwable is CustomException) {
            showMessage(throwable.exception ?: "Exception")
        }
    }

    override fun showSnackBar(message: String?) {

        mParentView?.let { parentView ->
            if (!message.isNullOrEmpty()) {
                val snackBar = make(parentView, message, Snackbar.LENGTH_LONG)
                snackBar.setActionTextColor(Color.RED)
                snackBar.show()
            }
        }
    }

    override fun showSnackBar(view: View, message: String) {

        runOnUiThread {
            val snackBar = make(view, message, Snackbar.LENGTH_LONG)
            snackBar.setActionTextColor(Color.RED)
            snackBar.show()
        }
    }

    override fun showFullScreenDialog(dialog: DialogFragment, bundle: Bundle?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        dialog.arguments = bundle
        dialog.show(fragmentTransaction, dialog.tag)
    }

    private var doubleBackToExitPressedOnce = false
    override fun doubleClickExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        showMessage(codeSnippet?.getString(R.string.double_click_to_exit))
        Timer().schedule(object : TimerTask() {
            override fun run() {
                doubleBackToExitPressedOnce = false
            }
        }, EXIT_TIMEOUT)
    }

    override fun showDefaultAlerter(
        alertText: String,
        alertIcon: Int,
        alertListener: DefaultAlertListener
    ) {
        val fragment = DefaultAlertFragment(
            DefaultAlertData(
                alertIcon,
                alertText
            ), alertListener
        )
        fragment.show(supportFragmentManager, fragment.tag)
    }

    override fun getExoPlayer(): ExoPlayerVideoHandler? {
        return ExoPlayerVideoHandler.getInstance()
    }

    override fun showForceUpdateDialog() {
        val fragment = ForceUpdateDialog(forceUpdateListener)
        fragment.show(supportFragmentManager, fragment.tag)
    }

    private var forceUpdateListener = object : ForceUpdateListener {
        override fun onClickUpdate() {

            try {
                startActivityWithIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${applicationContext.packageName}")
                    )
                )
            } catch (exception: ActivityNotFoundException) {
                startActivityWithIntent(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${applicationContext.packageName}")
                    )
                )
            }

            finishActivity()
        }
    }

    override fun showCustomDialog(dialogFragment: Fragment) {
        if (dialogFragment is BaseDialogFragment<*, *>) {
            dialogFragment.show(
                supportFragmentManager,
                dialogFragment.tag
            )
        }
    }

}