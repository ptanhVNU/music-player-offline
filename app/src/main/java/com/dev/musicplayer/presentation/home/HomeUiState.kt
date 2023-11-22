package com.dev.musicplayer.presentation.home

import com.dev.musicplayer.core.shared.models.MediaAudioItem

data class HomeUiState(
    val loading: Boolean? = false,
    val musics: List<MediaAudioItem>? = emptyList(),
    val selectedMusic: MediaAudioItem? = null,
    val errorMessage: String? = null
)
