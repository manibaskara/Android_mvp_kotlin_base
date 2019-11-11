package com.magnum.app.common

import android.app.Application
import com.magnum.app.BuildConfig
import com.magnum.app.util.MediaLoader
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import timber.log.Timber
import java.util.*

class BaseApplication : Application() {

    private var baseApplication: BaseApplication? = null
    override fun onCreate() {
        super.onCreate()
        baseApplication = this

        //To load images in gallery picker
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .setLocale(Locale.getDefault())
                .build()
        )

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun getInstance(): BaseApplication {
        if (baseApplication == null) {
            baseApplication = BaseApplication()
        }
        return baseApplication!!
    }
}