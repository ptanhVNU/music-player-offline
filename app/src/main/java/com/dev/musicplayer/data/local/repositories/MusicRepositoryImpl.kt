package com.dev.musicplayer.data.local.repositories

import com.dev.musicplayer.core.ext.toMusicEntity
import com.dev.musicplayer.core.ext.toSongEntity
import com.dev.musicplayer.core.services.LocalMediaProvider
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.data.local.store.SongStore
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.repositories.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(
    private val songStore: SongStore,
    private val localMediaProvider: LocalMediaProvider,
) : MusicRepository {
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /// Convert each MediaAudioItem to a SongEntity and save to ROOM Database
    override suspend fun insertSong(mediaAudioItems: List<MediaAudioItem>): Unit =
        withContext(Dispatchers.IO) {
            mediaAudioItems.map { mediaAudioItem ->
                songStore.insertSong(mediaAudioItem.toSongEntity())
            }
        }

    override fun getMusicsStorage(): Flow<List<MusicEntity>> {
        return localMediaProvider.getMediaAudiosFlow().map { mediaAudioItems ->
            /// After loading music from the storage device,
            /// save basic information such as URL, title, etc. into the database
            viewModelScope.launch(Dispatchers.IO) {
                insertSong(mediaAudioItems)
            }

            /// Convert each MediaAudioItem to a MusicEntity and create a list of MusicEntities
            /// using to presentation
            mediaAudioItems.map {
                it.toMusicEntity()
            }
        }
    }

    override fun cancelJobs() {
        viewModelJob.cancel()
    }
}

