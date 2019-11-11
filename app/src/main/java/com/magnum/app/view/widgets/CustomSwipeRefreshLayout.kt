package com.magnum.app.view.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.magnum.app.R
import com.magnum.app.util.CodeSnippet

class CustomSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {

    init {
        setColorSchemeColors(CodeSnippet(context).getColor(R.color.refresh_progress_3))
    }

}