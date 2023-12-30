package com.dev.musicplayer.data.local.store

import com.dev.musicplayer.data.local.dao.SongPlaylistsDao
import com.dev.musicplayer.data.local.entities.SongPlaylists
import javax.inject.Inject

class SongPlaylistStore @Inject constructor(
    private val songPlaylistsDao: SongPlaylistsDao,
) {

    suspend fun insertSongPlaylist(songPlaylists: SongPlaylists) =
        songPlaylistsDao.insert(songPlaylists)

    suspend fun deleteSongPlaylist(songPlaylists: SongPlaylists) =
        songPlaylistsDao.delete(songPlaylists)

    suspend fun getSongPlaylistBySongIdPlaylistId(songId: Long, playlistId: Long): SongPlaylists? =
        songPlaylistsDao.getSongPlaylistBySongIdAndPlaylistId(songId, playlistId)

    suspend fun getSongPlaylistsByPlaylistId(playlistId: Long): List<SongPlaylists>? =
        songPlaylistsDao.getSongPlaylistsByPlaylistId(playlistId)
}
