package com.dev.musicplayer.presentation.home

import com.dev.musicplayer.data.local.entities.Song

data class HomeUiState(
    val loading: Boolean? = false,
    val musics: List<Song>? = emptyList(),
    val selectedMusic: Song? = null,
    val errorMessage: String? = null
)
