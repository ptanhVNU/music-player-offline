package com.dev.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.SongPlaylists

@Dao
interface SongPlaylistsDao {
    @Upsert
    suspend fun insert(songPlaylists: SongPlaylists)

    /// delete song from playlist
    @Delete
    suspend fun delete(songPlaylists: SongPlaylists)

    @Query("SELECT * FROM song_playlists WHERE song_id = :songId AND playlist_id = :playlistId")
    suspend fun getSongPlaylistBySongIdAndPlaylistId(songId: Long, playlistId: Long): SongPlaylists?

    @Query("SELECT * FROM song_playlists WHERE playlist_id = :playlistId")
    suspend fun getSongPlaylistsByPlaylistId(playlistId: Long) : List<SongPlaylists>?
}