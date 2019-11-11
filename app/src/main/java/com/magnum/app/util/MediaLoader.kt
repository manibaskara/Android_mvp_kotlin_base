package com.magnum.app.util

import android.widget.ImageView
import com.magnum.app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader

/**
 * Used to load images in gallery picker with glide
 * */
class MediaLoader : AlbumLoader {

    override fun load(imageView: ImageView, albumFile: AlbumFile) {
        load(imageView, albumFile.path)
    }

    override fun load(imageView: ImageView, url: String) {

        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.placeholder))
            .into(imageView)
    }
}