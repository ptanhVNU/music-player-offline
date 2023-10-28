package com.dev.musicplayer.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Upsert
    suspend fun createPlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlist ORDER BY title ASC")
    fun getPlaylistsOrderedByName() : LiveData<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY created_at ASC")
    fun getPlaylistsOrderedByCreatedAt() : Flow<List<Playlist>>
}