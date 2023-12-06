package com.dev.musicplayer.presentation.playlist

import com.dev.musicplayer.data.local.entities.Playlist

data class PlaylistUiState (
    val loading : Boolean? = false,
    val playlists: List<Playlist>? = emptyList(),
    var sort : Boolean? = true,
)