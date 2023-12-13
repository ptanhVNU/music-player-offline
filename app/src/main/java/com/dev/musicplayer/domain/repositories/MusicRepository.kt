package com.dev.musicplayer.domain.repositories

import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.entities.MusicEntity
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun insertSong(mediaAudioItems : List<MediaAudioItem>)

    fun getMusicsStorage(): Flow<List<MusicEntity>>

    fun getLikedSongs() : Flow<List<Song>>

    fun cancelJobs()
}