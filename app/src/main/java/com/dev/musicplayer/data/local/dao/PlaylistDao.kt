package com.dev.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Upsert
    suspend fun createPlaylist(playlist: Playlist)
    @Query("DELETE from playlist WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long?)

    @Update
    suspend fun update(playlist: Playlist)

    @Query("SELECT * FROM playlist")
    fun getAllPlaylists(): Flow<List<Playlist>>


    @Query("SELECT songs FROM playlist WHERE id = :playlistId")
    fun getSongsOfPlaylist(playlistId: Long): Flow<List<String>>


    @Query("SELECT * FROM playlist ORDER BY title ASC")
    fun getPlaylistsOrderedByName() : Flow<List<Playlist>>

    @Query("SELECT * FROM playlist ORDER BY created_at ASC")
    fun getPlaylistsOrderedByCreatedAt() : Flow<List<Playlist>>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): Playlist

    @Query("SELECT * FROM playlist WHERE title = :title")
    suspend fun getPlaylistByName(title : String): Playlist

    @Query("SELECT * FROM playlist WHERE title LIKE '%' || :query || '%'")
    suspend fun searchPlaylists(query: String): List<Playlist>

}