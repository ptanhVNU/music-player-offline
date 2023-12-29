package com.dev.musicplayer.data.local.repositories

import com.dev.musicplayer.core.ext.toMusicEntity
import com.dev.musicplayer.core.ext.toSongEntity
import com.dev.musicplayer.core.services.LocalMediaProvider
import com.dev.musicplayer.core.shared.models.MediaAudioItem
import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.entities.SongPlaylists
import com.dev.musicplayer.data.local.store.SongPlaylistStore
import com.dev.musicplayer.data.local.store.SongStore
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.repositories.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(
    private val songStore: SongStore,
    private val songPlaylistStore: SongPlaylistStore,
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

        return flow {
            val mediaAudioItems = localMediaProvider.getMediaAudiosFlow().first()

            viewModelScope.launch(Dispatchers.IO) {
                insertSong(mediaAudioItems)
            }

            val allSongs = songStore.getAllSongs().map { songs ->
                songs.map { it.toMusicEntity() }
            }.first()

            emit(allSongs)
        }
        // Load music from storage device

//        return localMediaProvider.getMediaAudiosFlow().map { mediaAudioItems ->
//            /// After loading music from the storage device,
//            /// save basic information such as URL, title, etc. into the database
//
//            viewModelScope.launch(Dispatchers.IO) {
//                insertSong(mediaAudioItems)
//            }
//
//
//            /// Convert each MediaAudioItem to a MusicEntity and create a list of MusicEntities
//            /// using to presentation
////            mediaAudioItems.map {
////                it.toMusicEntity()
////            }
//            songStore.getAllSongs().map {
//                songs -> songs.map { it.toMusicEntity() }
//            }
//        }
    }

    override suspend fun addMusicToPlaylist(songId: Long, playlistId: Long) {
        val songPlaylist = SongPlaylists(songId = songId, playlistId = playlistId)

        viewModelScope.launch(Dispatchers.IO) {
            songPlaylistStore.insertSongPlaylist(songPlaylist)
        }


    }

    override suspend fun deleteMusicFromPlaylist(songId: Long, playlistId: Long) {
        val songPlaylist = songPlaylistStore.getSongPlaylistBySongIdPlaylistId(songId, playlistId)

        if (songPlaylist != null)
            songPlaylistStore.deleteSongPlaylist(songPlaylist)
    }

    override suspend fun getSongsByPlaylistId(playlistId: Long): List<MusicEntity> {
        val songPlaylists = songPlaylistStore.getSongPlaylistsByPlaylistId(playlistId)
        val songs = mutableListOf<Song>()

        if (songPlaylists == null) return emptyList<MusicEntity>()

        for (songPlaylist in songPlaylists) {
            val song = songStore.getSongById(songPlaylist.songId)
            songs.add(song!!)
        }

        return songs.map { it.toMusicEntity() }
    }


    override fun cancelJobs() {
        viewModelJob.cancel()
    }
}

