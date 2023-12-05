package com.dev.musicplayer.presentation.search

import com.dev.musicplayer.domain.entities.MusicEntity

data class SearchUiState(
    val loading: Boolean? = false,
    val musics: List<MusicEntity>? = emptyList(),
    val selectedMusic: MusicEntity = MusicEntity(
        id = "",
        title = "",
        artist =  "",
        source = "",
        image = "",
    ),
    val errorMessage: String? = null
)
