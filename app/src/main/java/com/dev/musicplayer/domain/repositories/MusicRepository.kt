package com.dev.musicplayer.domain.repositories

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun insertSong(title: String, uri: String)

    suspend fun deleteSong(song: Song)

    suspend fun editSong(song: Song)

    fun getAllSongs(): Flow<List<Song>>

    fun getSongsOrderedByName(): Flow<List<MusicEntity>>

    fun getSongsOrderedByCreatedAt(): Flow<List<MusicEntity>>

    fun getLikedSongs(): Flow<List<MusicEntity>>
}