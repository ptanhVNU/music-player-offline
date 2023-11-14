package com.dev.musicplayer.core.services

import android.app.PendingIntent
import android.content.ContentResolver
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MusicPlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    private lateinit var exoPlayer: ExoPlayer

    private val audioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    /**
     * The PendingIntent associated with the underlying activity.
     * It launches the main activity when triggered.
     */
    private val activity by lazy {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    @UnstableApi
    override fun onCreate() {
        super.onCreate()

        initExoPlayer()
        mediaSession = MediaSession.Builder(this, exoPlayer)
            .setSessionActivity(activity)
            .setCallback(MediaSessionCallback())
            .build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun getContentResolver(): ContentResolver {
        return super.getContentResolver()
    }

    override fun onDestroy() {
        mediaSession?.run {
            exoPlayer.release()
            release()
            mediaSession = null
        }

        super.onDestroy()
    }

    private fun initExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }

    private inner class MediaSessionCallback : MediaSession.Callback {
        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            val updatedMediaItems = mediaItems.map {
                it.buildUpon().setUri(it.mediaId).build()
            }.toMutableList()

            return Futures.immediateFuture(updatedMediaItems)
        }
    }
}