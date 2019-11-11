package com.magnum.app.view.widgets

import android.graphics.drawable.Drawable

import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util

abstract class CustomTarget<T>
/**
 * Creates a new `CustomTarget` that will return the given `width` and [@code]
 * as the requested size (unless overridden by
 * [com.bumptech.glide.request.RequestOptions.override] in the request).
 *
 * @param width The requested width (>= 0, or == Target.SIZE_ORIGINAL).
 * @param height The requested height (>= 0, or == Target.SIZE_ORIGINAL).
 */
@JvmOverloads constructor(
    private val width: Int = Target.SIZE_ORIGINAL,
    private val height: Int = Target.SIZE_ORIGINAL
) : Target<T> {

    private var request: Request? = null

    init {
        require(Util.isValidDimensions(width, height)) {
            ("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given" + " width: "
                    + width + " and height: " + height)
        }
    }

    override fun onStart() {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onStop() {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onDestroy() {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun getSize(cb: SizeReadyCallback) {
        cb.onSizeReady(width, height)
    }

    override fun removeCallback(cb: SizeReadyCallback) {
        // Do nothing, this class does not retain SizeReadyCallbacks.
    }

    override fun setRequest(request: Request?) {
        this.request = request
    }

    override fun getRequest(): Request? {
        return request
    }
}
