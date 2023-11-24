package com.dev.musicplayer.presentation.home

import com.dev.musicplayer.domain.entities.MusicEntity

data class HomeUiState(
    val loading: Boolean? = false,
    val musics: List<MusicEntity>? = emptyList(),
    val selectedMusic: MusicEntity? = null,
    val errorMessage: String? = null
)
