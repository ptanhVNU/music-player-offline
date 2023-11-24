package com.dev.musicplayer.data.local.repositories

import com.dev.musicplayer.core.ext.toMusicEntity
import com.dev.musicplayer.core.services.LocalMediaProvider
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.store.SongStore
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.repositories.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(
    private val songStore: SongStore,
    private val localMediaProvider: LocalMediaProvider,
) : MusicRepository {
    // songs
    override suspend fun insertSong(title: String, uri: String) = withContext(Dispatchers.IO) {
        val song = Song(
            title = title,
            uri = uri,
            createdAt = Instant.now().toEpochMilli(),
        )
        songStore.insertSong(song)
    }


    override suspend fun editSong(song: Song) = withContext(Dispatchers.IO) {
        songStore.editSong(song)
    }

    override fun getMusicsStorage(): Flow<List<MusicEntity>> {
        return localMediaProvider.getMediaAudiosFlow().map { mediaAudioItems ->
            mediaAudioItems.map { it.toMusicEntity() }
        }
    }
}

