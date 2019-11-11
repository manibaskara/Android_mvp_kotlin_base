package com.magnum.app.view.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.magnum.app.R
import com.magnum.app.adapter.listener.DefaultAlertListener
import com.magnum.app.presenter.ipresenter.IPresenter
import com.magnum.app.util.CodeSnippet
import com.magnum.app.util.CustomException
import com.magnum.app.util.sharepreference.SharedPrefRepository
import com.magnum.app.view.iview.IBaseDialogFragment
import com.magnum.app.view.iview.IView
import com.magnum.app.view.widgets.CustomProgressBar
import com.magnum.app.view.widgets.exoplayer.ExoPlayerVideoHandler


abstract class BaseDialogFragment<IP : IPresenter, VB : ViewDataBinding> : DialogFragment(),
    IBaseDialogFragment {

    protected var iPresenter: IP? = null
    protected var viewDataBinding: VB? = null
    abstract fun getLayoutRes(): Int
    abstract fun onInitializePresenter(): IP
    private var customProgressBar: CustomProgressBar? = null
    private var mCodeSnippet: CodeSnippet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        val root = viewDataBinding?.root
        iPresenter = onInitializePresenter()

        if (iPresenter != null) {
            mCodeSnippet = activity?.applicationContext?.let { CodeSnippet(it) }
            onPresenterInitialized()
            iPresenter?.onCreate(arguments)
            dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return root
    }

    override fun finishActivity() {
        if (activity is IView)
            (activity as IView).finishActivity()

    }

    override fun pauseVideo() {
        if (activity is IView)
            (activity as IView).pauseVideo()
    }

    open fun onPresenterInitialized() {
    }

    override fun onPause() {
        super.onPause()
        iPresenter?.onPause()
    }


    override fun onResume() {
        super.onResume()
        iPresenter?.onResume()

        val window = dialog?.window
        window?.let {
            val size = Point()
            val display = it.windowManager.defaultDisplay
            display.getSize(size)
            val width = size.x
            it.setLayout((width * 0.8).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            it.setGravity(Gravity.CENTER)
        }

    }

    override fun showCustomDialog(dialogFragment: Fragment) {
        if (activity is IView)
            (activity as IView).showCustomDialog(dialogFragment)

    }

    /*  override fun getSharedPreferences(): SharedPrefsManager {
          return (activity as IView).getSharedPreferences()
      }*/
    override val codeSnippet: CodeSnippet?
        get() = if (activity is IView) (activity as IView).codeSnippet else null


  /*  override val sharedPrefManager: SharedPrefsManager
        get() = (activity as IView).sharedPrefManager
*/
    override fun showForceUpdateDialog() {
        if (activity is IView)
            (activity as IView).showForceUpdateDialog()
    }

    override fun onDestroy() {
        super.onDestroy()

        iPresenter?.onDestroy()
    }

    override fun showMessage(message: CustomException) {
        if (activity is IView)
            (activity as IView).showMessage(message)
    }

    override fun showMessage(throwable: Throwable) {
        if (activity is IView)
            (activity as IView).showMessage(throwable)
    }

    override fun showProgressbar() {
        getProgressBar()?.show()
    }

    override fun dismissProgressbar() {
        getProgressBar()?.dismiss()
    }

    override fun showNetworkMessage() {
        if (activity is IView)
            (activity as IView).showNetworkMessage()
    }

    override fun showMessage(message: String?) {
        if (activity is IView)
            (activity as IView).showMessage(message)
    }

    override fun showMessage(message: Int) {
        if (activity is IView)
            (activity as IView).showMessage(message)
    }


    private fun getProgressBar(): CustomProgressBar? {

        if (customProgressBar == null)
            customProgressBar = CustomProgressBar(requireContext())

        return customProgressBar
    }

    override fun showDefaultAlerter(
        alertText: String,
        alertIcon: Int,
        alertListener: DefaultAlertListener
    ) {
        if (activity is IView)
            (activity as IView).showDefaultAlerter(alertText, alertIcon, alertListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        iPresenter?.onActivityResult(requestCode, resultCode, data)
    }

    override fun hideKeyBoard() {
        if (activity is IView)
            (activity as IView).hideKeyBoard()
    }


    override fun showSnackBar(message: String?) {
        if (activity is IView)
            (activity as IView).showSnackBar(message)
    }

    override fun showSnackBar(view: View, message: String) {
        if (activity is IView)
            (activity as IView).showSnackBar(view, message)
    }

    override fun replaceFragment(layoutId: Int, fragment: Fragment, fragmentTag: String) {
        if (activity is IView)
            (activity as IView).replaceFragment(layoutId, fragment, fragmentTag)
    }

    override fun replaceFragmentWithAnimation(
        layoutId: Int,
        fragment: Fragment,
        animationDirection: Int
    ) {
        if (activity is IView)
            (activity as IView).replaceFragmentWithAnimation(layoutId, fragment, animationDirection)
    }


    override fun replaceFragmentWithBackStack(
        layoutId: Int,
        fragment: Fragment,
        currentFragment: Fragment,
        fragmentCode: Int,
        fragmentTag: String, needDefaultAnim: Boolean

    ) {
        if (activity is IView)
            (activity as IView).replaceFragmentWithBackStack(
                layoutId,
                fragment,
                currentFragment,
                fragmentCode,
                fragmentTag,
                needDefaultAnim
            )
    }

    override fun onClickBack() {
        if (activity is IView)
            (activity as IView).onClickBack()

    }

    override fun showFullScreenDialog(dialog: DialogFragment, bundle: Bundle?) {
        if (activity is IView)
            (activity as IView).showFullScreenDialog(dialog, bundle)
    }

    override fun dismissDialog() {
        dialog?.dismiss()
    }

    override fun <G> navigateTo(className: Class<G>, bundle: Bundle?, needDefaultAnim: Boolean) {
        if (activity is IView)
            (activity as IView).navigateTo(className, bundle, needDefaultAnim)
    }

    override fun navigateToHome(bundle: Bundle?, needDefaultAnim: Boolean) {
        if (activity is IView)
            (activity as IView).navigateToHome(bundle, needDefaultAnim)
    }

    override fun startActivityWithIntent(intent: Intent) {
        if (activity is IView)
            (activity as IView).startActivityWithIntent(intent)
    }

    override fun <G> navigateToWithResult(
        className: Class<G>,
        bundle: Bundle?,
        needDefaultAnim: Boolean,
        activityRequestCode: Int
    ) {
        //should start intent from fragment to get onActivityResult in fragment
        val intent = Intent(activity, className)
        if (bundle != null)
            intent.putExtras(bundle)
        startActivityForResult(intent, activityRequestCode)
        if (needDefaultAnim)
            activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_out_left)
    }

    override fun showKeyBoard(view: View) {
        if (activity is IView)
            (activity as IView).showKeyBoard(view)
    }

    override fun setFragment(fragment: Fragment, container: Int, fragmentTag: String) {
        if (activity is IView)
            (activity as IView).setFragment(fragment, container, fragmentTag)
    }


    override fun doubleClickExit() {
        if (activity is IView)
            (activity as IView).doubleClickExit()
    }

    override fun getExoPlayer(): ExoPlayerVideoHandler? {
        return if (activity is IView)
            (activity as IView).getExoPlayer()
        else null
    }

    override val sharedPrefRepository: SharedPrefRepository?
        get() = if (activity is IView) (activity as IView).sharedPrefRepository else null
}