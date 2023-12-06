package com.dev.musicplayer.data.local.store

import android.app.Application
import com.dev.musicplayer.data.local.MusicAppDatabase
import com.dev.musicplayer.data.local.dao.PlaylistDao
import com.dev.musicplayer.data.local.entities.Playlist
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistStore @Inject constructor(
    private val playlistDao : PlaylistDao,
) {
    suspend fun createPlaylist(playlist: Playlist) = playlistDao.createPlaylist(playlist)

    fun getAllPlaylists(): Flow<List<Playlist>> = playlistDao.getAllPlaylists()

    suspend fun deletePlaylist(playlistId: Long) = playlistDao.deletePlaylist(playlistId)

    fun getPlaylistsOrderedByName() = playlistDao.getPlaylistsOrderedByName()

    fun getPlaylistsOrderedByCreatedAt() = playlistDao.getPlaylistsOrderedByCreatedAt()

    suspend fun getPlaylistById(playlistId: Long) = playlistDao.getPlaylistById(playlistId)

    suspend fun update(playlist: Playlist) = playlistDao.update(playlist)

    suspend fun getPlaylistByName(title : String) = playlistDao.getPlaylistByName(title)


}