package com.dev.musicplayer.core.shared.components

import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.utils.PlayerState

data class MusicPlaybackUiState(
    val playerState: PlayerState? = PlayerState.PAUSED,
    val currentMusic: MediaAudioItem? = null,
    val currentPosition: Long = 0L,
    val totalDuration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val isRepeatOneEnabled: Boolean = false
)
