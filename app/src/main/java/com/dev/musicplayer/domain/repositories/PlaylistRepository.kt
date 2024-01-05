package com.dev.musicplayer.domain.repositories

import android.app.Application
import androidx.room.Room
import com.dev.musicplayer.data.local.MusicAppDatabase
import com.dev.musicplayer.data.local.MusicAppDatabase_Impl
import com.dev.musicplayer.data.local.dao.PlaylistDao
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.domain.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun createPlaylist(title: String)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun update(playlist: Playlist)

    fun getPlaylistsOrderedByName() : Flow<List<Playlist>>

    suspend fun getSongsOfPlaylist(playlistId: Long): Flow<List<String>>

    fun getPlaylistsOrderedByCreatedAt() : Flow<List<Playlist>>

    suspend fun getPlaylistById(playlistId : Long): Playlist

    suspend fun getPlaylistByName(title : String): Playlist

    suspend fun deleteSongFromPlaylist(playlistId: Long, musicEntityJson: String)
}
