package com.dev.musicplayer.data.local.reposity

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.store.SongStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val songStore: SongStore
) {
    // songs
    suspend fun insertSong(song: Song) = withContext(Dispatchers.IO) {
        songStore.insertSong(song)
    }

    suspend fun deleteSong(song: Song) = withContext(
        Dispatchers.IO
    ) {
        songStore.deleteSong(song)
    }

    suspend fun getAllSongs() = withContext(Dispatchers.IO) {
        songStore.getAllSongs()
    }

    suspend fun getSongsOrderedByName() = withContext(Dispatchers.IO) {
        songStore.getSongsOrderedByName()
    }

    suspend fun getSongsOrderedByCreatedAt() = withContext(Dispatchers.IO) {
        songStore.getSongsOrderedByCreatedAt()
    }

    suspend fun getLikedSongs() = withContext(Dispatchers.IO) {
        songStore.getLikedSongs()
    }

    // playlist

}