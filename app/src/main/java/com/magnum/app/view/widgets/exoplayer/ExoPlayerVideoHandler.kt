package com.magnum.app.view.widgets.exoplayer

import android.content.Context
import android.net.Uri
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import com.magnum.app.util.CodeSnippet
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import timber.log.Timber

class ExoPlayerVideoHandler private constructor() {
    var videoPlayer: SimpleExoPlayer? = null
    private var videoUrl: String? = null
    private var videoSurfaceView: PlayerView? = null
    private var exoPlayerListener: ExoPlayerListener? = null
    private var codeSnippet: CodeSnippet? = null
    fun prepareVideoUrl(
        context: Context?,
        videoUrl: String?,
        mediaContainer: FrameLayout?,
        exoPlayerListener: ExoPlayerListener
    ) {
        if (context != null && videoUrl != null && mediaContainer != null) {

            //releasing the existing player
            codeSnippet = CodeSnippet(context)
            codeSnippet?.pauseOtherSounds()
            releaseVideoPlayer()

            this.videoUrl = videoUrl

            //creating Video Source with url
            val dataSourceFactory =
                DefaultDataSourceFactory(context, Util.getUserAgent(context, "Skinella"))
            val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(this.videoUrl))

            //initializing videoSurfaceView
            videoSurfaceView = PlayerView(context)
            videoSurfaceView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            videoSurfaceView?.useController = true
            videoSurfaceView?.setRepeatToggleModes(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            //initializing VideoPlayer
            videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
            videoPlayer?.repeatMode = Player.REPEAT_MODE_OFF

            videoPlayer?.prepare(videoSource)
            videoPlayer?.clearVideoSurface()
            videoPlayer?.setVideoSurfaceView(videoSurfaceView?.videoSurfaceView as SurfaceView)
            videoSurfaceView?.player = videoPlayer
            addVideoListener(exoPlayerListener)
            mediaContainer.addView(videoSurfaceView)
            videoPlayer?.playWhenReady = true
        }
    }

    fun prepareExoPlayerForLocal(
        context: Context?,
        videoUri: Uri?,
        mediaContainer: FrameLayout?,
        exoPlayerListener: ExoPlayerListener
    ) {
        if (context != null && videoUri != null && mediaContainer != null) {

            //releasing the existing player
            releaseVideoPlayer()

            //creating Video Source with url
            val dataSourceFactory =
                DefaultDataSourceFactory(context, Util.getUserAgent(context, "Skinella"))
            val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri)

            //initializing videoSurfaceView
            videoSurfaceView = PlayerView(context)
            videoSurfaceView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            videoSurfaceView?.useController = true
            videoSurfaceView?.setRepeatToggleModes(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory()
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            //initializing VideoPlayer
            videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
            videoPlayer?.repeatMode = Player.REPEAT_MODE_OFF

            videoPlayer?.prepare(videoSource)
            videoPlayer?.clearVideoSurface()
            videoPlayer?.setVideoSurfaceView(videoSurfaceView?.videoSurfaceView as SurfaceView)
            videoSurfaceView?.player = videoPlayer
            addVideoListener(exoPlayerListener)
            mediaContainer.addView(videoSurfaceView)
            videoPlayer?.playWhenReady = true
        }
    }

    private fun addVideoListener(listener: ExoPlayerListener) {
        this.exoPlayerListener = listener
        videoPlayer?.addListener(object : Player.EventListener {

            override fun onPlayerError(error: ExoPlaybackException?) {

            }

            override fun onSeekProcessed() {
                Timber.d("onPlayerStateChanged: Seek Processed.")
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {

                    Player.STATE_BUFFERING -> {
                        Timber.d("onPlayerStateChanged: Buffering video.")
                        exoPlayerListener?.onBuffering()

                    }
                    Player.STATE_ENDED -> {
                        Timber.d("onPlayerStateChanged: Video ended.")
                        exoPlayerListener?.onVideoEnded()
                    }

                    Player.STATE_READY -> {
                        codeSnippet?.pauseOtherSounds()
                        Timber.d("onPlayerStateChanged: Ready to play.")
                        exoPlayerListener?.onReadyToPlay()

                    }

                }
            }
        })
    }

    fun releaseVideoPlayer() {
        videoPlayer?.release()
        exoPlayerListener?.onReleased()
        if (videoSurfaceView?.parent is ViewGroup) {
            val parent = videoSurfaceView?.parent as ViewGroup
            val index = parent.indexOfChild(videoSurfaceView)
            if ((index >= 0)) {
                parent.removeViewAt(index)
            }
        }

        videoSurfaceView = null
        videoPlayer = null
        exoPlayerListener = null
    }

    fun pauseVideo() {
        if (isPlaying()) {
            videoPlayer?.playWhenReady = false
            videoPlayer?.seekTo(videoPlayer?.currentPosition ?: 0)
        }

    }

    private fun isPlaying(): Boolean {
        return videoPlayer?.playWhenReady ?: false
    }

    companion object {
        private var instance: ExoPlayerVideoHandler? = null

        fun getInstance(): ExoPlayerVideoHandler {
            if (instance == null) {
                instance = ExoPlayerVideoHandler()
            }
            return instance!!
        }
    }
}