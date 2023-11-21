package com.dev.musicplayer.domain.repositories

import com.dev.musicplayer.data.local.entities.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(title: String)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun deletePlaylist(playlist: Playlist)

//    fun getPlaylistsOrderedByName() : Flow<List<PlaylistEntity>>
//
//    fun getPlaylistsOrderedByCreatedAt() : Flow<List<PlaylistEntity>>

    suspend fun getPlaylistById(playlistId : Long): Playlist
}