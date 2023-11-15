package com.dev.musicplayer.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Upsert
    suspend fun createPlaylist(playlist: Playlist)
    @Query("DELETE from playlist WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long?)

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY title ASC")
    fun getPlaylistsOrderedByName() : Flow<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY created_at ASC")
    fun getPlaylistsOrderedByCreatedAt() : Flow<List<Playlist>>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): Playlist

}