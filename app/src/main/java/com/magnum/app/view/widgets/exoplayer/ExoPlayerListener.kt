package com.magnum.app.view.widgets.exoplayer

interface ExoPlayerListener {

    fun onBuffering()

    fun onVideoEnded()

    fun onReadyToPlay()

    fun onReleased()

    fun onPlayerError(message : String)

}