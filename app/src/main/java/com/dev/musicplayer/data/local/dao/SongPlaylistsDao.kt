package com.dev.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.dev.musicplayer.data.local.entities.SongPlaylists

@Dao
interface SongPlaylistsDao {
    @Upsert
    suspend fun insert(songPlaylists: SongPlaylists)
}