package com.dev.musicplayer.core.services

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.service.PlaybackController
import com.dev.musicplayer.utils.PlayerState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MusicPlaybackController(context: Context) : PlaybackController {

    private var mediaControllerFuture: ListenableFuture<MediaController>
    private val mediaController: MediaController?
        get() = if (mediaControllerFuture.isDone) mediaControllerFuture.get() else null

    override var mediaControllerCallback: ((PlayerState, MediaAudioItem?, Long, Long, Boolean, Boolean) -> Unit)? = null

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
                        currentMediaItem?.toMediaAudioItem(),
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


    override fun addMediaItemsFromStorage(musics: List<MediaAudioItem>) {
        val mediaItems = musics.map {
            MediaItem.Builder()
                .setMediaId(it.absolutePath)
                .setUri(it.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(it.name)
                        .setArtist(it.artist)
                        .build()
                )
                .build()
        }

        mediaController?.setMediaItems(mediaItems)

        Log.d("TAG", "addMediaItems: ${mediaItems.size}")
    }

    override fun play(mediaItemIndex: Int) {

        mediaController?.apply {
            seekToDefaultPosition(mediaItemIndex)
            Log.d("Music Playback Controller", "play index: $mediaItemIndex")
            playWhenReady = true
            prepare()

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


fun MediaItem.toSong() = Song(
//    id = mediaId,
    title = mediaMetadata.title.toString(),
    artistName = mediaMetadata.artist.toString(),
    uri = mediaId,
    thumbnail = mediaMetadata.artworkUri.toString(),
)

fun MediaItem.toMediaAudioItem() = MediaAudioItem(
    id = mediaId.toLong(),
    name = mediaMetadata.title.toString(),
    artist = mediaMetadata.artist.toString(),
    absolutePath = mediaId,
    artWork = null,
    duration = 0L,
    uri = mediaId.toUri(),
    size = 0L,
)