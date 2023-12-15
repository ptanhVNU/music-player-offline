package com.dev.musicplayer.presentation.search

import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity

data class SearchResult(
    val songs: List<MusicEntity> = emptyList(),
    val playlists: List<Playlist> = emptyList(),
    val errorMsg: String? = null
)