package com.dev.musicplayer.core.shared.components

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.utils.PlayerState

data class MusicPlaybackUiState(
    val playerState: PlayerState? = null,
    val currentMusic: Song? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)
