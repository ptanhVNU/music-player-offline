package com.dev.musicplayer.data.local.reposity

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.store.SongStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val songStore: SongStore,

) {
    // songs
    suspend fun insertSong(song: Song)  {
        return songStore.insertSong(song)
    }

    suspend fun deleteSong(song: Song) = songStore.deleteSong(song)

    fun getAllSongs() = songStore.getLikedSongs()

    fun getSongsOrderedByName() = songStore.getSongsOrderedByName()

    fun getSongsOrderedByCreatedAt() = songStore.getSongsOrderedByCreatedAt()

    fun getLikedSongs() = songStore.getLikedSongs()


    // playlist

}