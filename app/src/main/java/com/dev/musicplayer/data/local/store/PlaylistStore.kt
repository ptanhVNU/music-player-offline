package com.dev.musicplayer.data.local.store

import com.dev.musicplayer.data.local.dao.PlaylistDao
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import javax.inject.Inject

class PlaylistStore @Inject constructor(
    private val playlistDao : PlaylistDao
) {
    suspend fun createPlaylist(playlist: Playlist) = playlistDao.createPlaylist(playlist)

    fun getAllPlaylists() = playlistDao.getAllPlaylists()

    suspend fun deletePlaylist(playlistId: Long) = playlistDao.deletePlaylist(playlistId)

    fun getPlaylistsOrderedByName() = playlistDao.getPlaylistsOrderedByName()

    fun getPlaylistsOrderedByCreatedAt() = playlistDao.getPlaylistsOrderedByCreatedAt()

    suspend fun getPlaylistById(playlistId: Long) = playlistDao.getPlaylistById(playlistId)

}