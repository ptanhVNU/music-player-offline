package com.dev.musicplayer.domain.repositories

import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.domain.entities.MusicEntity
import kotlinx.coroutines.flow.Flow

interface MusicRepository {
    suspend fun insertSong(mediaAudioItems : List<MediaAudioItem>)

    fun getMusicsStorage(): Flow<List<MusicEntity>>



    suspend fun addMusicToPlaylist(songId: Long, playlistId: Long)

    suspend fun deleteMusicFromPlaylist(songId: Long, playlistId: Long)

    suspend fun getSongsByPlaylistId(playlistId: Long) : List<MusicEntity>

    fun cancelJobs()
}