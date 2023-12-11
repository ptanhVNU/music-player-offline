package com.dev.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Upsert
    suspend fun insertSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Update
    suspend fun editSong(song: Song)

    @Query("SELECT * FROM song ")
    fun getAllSongs(): Flow<List<Song>>


    @Query("SELECT * FROM song ORDER BY title ASC")
    fun getSongsOrderedByName(): Flow<List<Song>>

    @Query("SELECT * FROM song ORDER BY created_at ASC")
    fun getSongsOrderedByCreatedAt(): Flow<List<Song>>

    @Query("SELECT * FROM song WHERE is_liked = 1")
    fun getLikedSongs(): Flow<List<Song>>


    @Query("SELECT * FROM song WHERE title LIKE '%' || :query || '%'")
    suspend fun searchSongs(query: String): List<Song>

}