package com.magnum.app.view.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.magnum.app.R
import com.magnum.app.util.CodeSnippet

class CustomProgressBar : Dialog {

    @Throws(NullPointerException::class)
    constructor(context: Context) : super(context) {
        createProgressBar(context)
    }

    @Throws(NullPointerException::class)
    constructor(context: Context, text: String) : super(context) {
        createProgressBar(context, text)
    }

    @Throws(NullPointerException::class)
    private fun createProgressBar(context: Context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getSmallProgressbar(context))
        val window = window
        if (window != null) {
            window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            window.setBackgroundDrawableResource(android.R.color.transparent)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        } else {
            throw NullPointerException("window not fount exception!")
        }
    }

    @Throws(NullPointerException::class)
    private fun createProgressBar(context: Context, text: String) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getSmallProgressbarWithText(context, text))
        val window = window
        if (window != null) {
            window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            window.setBackgroundDrawableResource(android.R.color.transparent)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        } else {
            throw NullPointerException("window not fount exception!")
        }
    }

    private fun getSmallProgressbar(context: Context): View {
        val relativeLayout = FrameLayout(context)
        val params = FrameLayout.LayoutParams(160, 160)

        val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyle)
        progressBar.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        progressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)

        val progressBar1 = ProgressBar(context, null, android.R.attr.progressBarStyle)
        progressBar1.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        progressBar1.setPadding(24, 24, 24, 24)
        params.gravity = Gravity.CENTER

        val progressBar2 = ProgressBar(context, null, android.R.attr.progressBarStyle)
        progressBar2.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        progressBar2.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        progressBar2.setPadding(48, 48, 48, 48)
        params.gravity = Gravity.CENTER

        relativeLayout.addView(progressBar2, params)
        relativeLayout.addView(progressBar1, params)
        relativeLayout.addView(progressBar, params)

        return relativeLayout
    }

    private fun getSmallProgressbarWithText(context: Context, text: String): View {
        val relativeLayout = FrameLayout(context)
        val params = FrameLayout.LayoutParams(150, 150)

        val progressBar = ProgressBar(
            context, null,
            android.R.attr.progressBarStyle
        )
        progressBar.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        progressBar.indeterminateDrawable =
            CodeSnippet(context).getDrawable(R.drawable.loading_progress)

        val imageView = ImageView(context, null)
        imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imageView.setPadding(32, 32, 32, 32)
        imageView.setImageResource(R.drawable.loading_progress)
        params.gravity = Gravity.CENTER

        val textView = TextView(context, null)
        textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.setPadding(2, 2, 2, 2)
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
        params.gravity = Gravity.CENTER

        relativeLayout.addView(imageView, params)
        relativeLayout.addView(progressBar, params)
        relativeLayout.addView(textView, params)

        return relativeLayout
    }

    @Throws(Exception::class)
    fun dismissProgress() {
        super.dismiss()
    }

}