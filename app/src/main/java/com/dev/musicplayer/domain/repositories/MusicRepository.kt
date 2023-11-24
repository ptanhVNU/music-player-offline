package com.dev.musicplayer.domain.repositories

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun insertSong(title: String, uri: String)
    suspend fun editSong(song: Song)

    fun getMusicsStorage(): Flow<List<MusicEntity>>
}