package com.dev.musicplayer.core.services

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.dev.musicplayer.core.ext.toMusicEntity
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.service.PlaybackController
import com.dev.musicplayer.utils.PlayerState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MusicPlaybackController(context: Context) : PlaybackController {

    private var mediaControllerFuture: ListenableFuture<MediaController>
    private val mediaController: MediaController?
        get() = if (mediaControllerFuture.isDone) mediaControllerFuture.get() else null

    override var mediaControllerCallback: (
        (playerState: PlayerState,
         currentMusic: MusicEntity?,
         currentPosition: Long,
         totalDuration: Long,
         isShuffleEnabled: Boolean,
         isRepeatOneEnabled: Boolean) -> Unit
    )? = null

    init {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlaybackService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture.addListener({ controllerListener() }, MoreExecutors.directExecutor())
    }

    private fun controllerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                with(player) {
                    mediaControllerCallback?.invoke(
                        playbackState.toPlayerState(isPlaying),
                        currentMediaItem?.toMusicEntity(),
                        currentPosition.coerceAtLeast(0L),
                        duration.coerceAtLeast(0L),
                        shuffleModeEnabled,
                        repeatMode == Player.REPEAT_MODE_ONE
                    )
                }
            }
        })
    }

    private fun Int.toPlayerState(isPlaying: Boolean) =
        when (this) {
            Player.STATE_IDLE -> PlayerState.STOPPED
            Player.STATE_ENDED -> PlayerState.STOPPED
            else -> if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
        }

    override fun addMediaItems(musics: List<MusicEntity>) {
        val mediaItems = musics.map {
            MediaItem.Builder()
                .setMediaId(it.source)
                .setUri(it.source)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.title)
                        .setArtist(it.artist)
                        .setArtworkUri(Uri.parse(it.image))
                        .build()
                )
                .build()
        }
        mediaController?.clearMediaItems()
        mediaController?.setMediaItems(mediaItems)
    }

    override fun play(mediaItemIndex: Int) {
        mediaController?.apply {
            seekToDefaultPosition(mediaItemIndex)
            playWhenReady = true
            prepare()
            play()
        }
    }

    override fun resume() {
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }

    override fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    override fun skipNext() {
        mediaController?.seekToNext()
    }

    override fun skipPrevious() {
        mediaController?.seekToPrevious()
    }

    override fun setShuffleModeEnabled(isEnabled: Boolean) {
        mediaController?.shuffleModeEnabled = isEnabled
    }

    override fun setRepeatOneEnabled(isEnabled: Boolean) {
        mediaController?.repeatMode = if (isEnabled) {
            Player.REPEAT_MODE_ONE
        } else {
            Player.REPEAT_MODE_OFF
        }
    }

    override fun getCurrentPosition() = mediaController?.currentPosition ?: 0L

    override fun destroy() {
        MediaController.releaseFuture(mediaControllerFuture)
        mediaControllerCallback = null
    }
}