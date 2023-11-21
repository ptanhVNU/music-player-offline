package com.dev.musicplayer.domain.repositories

import com.dev.musicplayer.data.local.entities.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun insertSong(title: String, uri: String)

    suspend fun deleteSong(song: Song)

    fun getAllSongs(): Flow<List<Song>>

}