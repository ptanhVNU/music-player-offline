package com.dev.musicplayer.domain.service

import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.utils.PlayerState

interface PlaybackController {
    var mediaControllerCallback: (
        (
        playerState: PlayerState,
        currentMusic: MusicEntity?,
        currentPosition: Long,
        totalDuration: Long,
        isShuffleEnabled: Boolean,
        isRepeatOneEnabled: Boolean
    ) -> Unit
    )?

    fun addMediaItems(musics: List<MusicEntity>)

    fun play(mediaItemIndex: Int)

    fun resume()

    fun pause()

    fun seekTo(position: Long)

    fun clear()

    fun skipNext()

    fun skipPrevious()

    fun setShuffleModeEnabled(isEnabled: Boolean)

    fun setRepeatOneEnabled(isEnabled: Boolean)

    fun getCurrentPosition(): Long

    fun destroy()
}