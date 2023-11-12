package com.dev.musicplayer.domain.service

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.utils.PlayerState

interface PlaybackController {
    var mediaControllerCallback: ((PlayerState, Song?, Long, Long, Boolean, Boolean) -> Unit)?

    fun addMediaItems(musics: List<Song>)

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