package com.dev.musicplayer.core.shared.components

import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.utils.PlayerState

data class MusicPlaybackUiState(
    val playerState: PlayerState? = null,
    val currentMusic: MusicEntity? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)
