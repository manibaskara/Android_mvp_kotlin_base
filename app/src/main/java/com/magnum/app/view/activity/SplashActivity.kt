package com.magnum.app.view.activity

import android.view.View
import android.view.WindowManager
import com.magnum.app.R
import com.magnum.app.common.Constants.CommonKeys.Companion.SPLASH_TIME_OUT
import com.magnum.app.databinding.ActivitySplashBinding
import com.magnum.app.presenter.SplashPresenter
import com.magnum.app.presenter.ipresenter.ISplashPresenter
import com.magnum.app.view.iview.ISplashView
import java.util.*

class SplashActivity : BaseActivity<ISplashPresenter, ActivitySplashBinding>(), ISplashView {

    override fun getLayoutRes(): Int {
        return R.layout.activity_splash
    }

    override fun onInitializePresenter(): ISplashPresenter {
        return SplashPresenter(this)
    }

    override fun onPresenterInitialized() {

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                iPresenter?.checkForceUpdate()
            }
        }, SPLASH_TIME_OUT)


    }

    override fun dismissProgressbar() {
        viewDataBinding?.progressBar?.post {
            viewDataBinding?.progressBar?.visibility = View.GONE
        }
    }

    override fun showProgressbar() {
        viewDataBinding?.progressBar?.post {
            viewDataBinding?.progressBar?.visibility = View.VISIBLE
        }
    }
}