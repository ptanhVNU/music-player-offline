package com.dev.musicplayer.presentation.playlist

import com.dev.musicplayer.data.local.entities.Playlist

sealed class PlaylistEvent {
    data class SelectedPlaylist(val selectedPlaylist: Playlist) : PlaylistEvent()
    data class SwipeTdoDelete(val deletedPlaylist: Playlist) : PlaylistEvent()
}