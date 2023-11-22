package com.dev.musicplayer.domain.service

import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.utils.PlayerState

interface PlaybackController {
    var mediaControllerCallback: ((PlayerState, MediaAudioItem?, Long, Long, Boolean, Boolean) -> Unit)?


    fun addMediaItemsFromStorage(musics: List<MediaAudioItem>)

    fun play(mediaItemIndex: Int)

    fun resume()

    fun pause()

    fun seekTo(position: Long)

    fun skipNext()

    fun skipPrevious()

    fun setShuffleModeEnabled(isEnabled: Boolean)

    fun setRepeatOneEnabled(isEnabled: Boolean)

    fun getCurrentPosition(): Long

    fun destroy()
}