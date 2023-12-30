package com.dev.musicplayer.data.local.store

import com.dev.musicplayer.data.local.dao.SongDao
import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.entities.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongStore @Inject constructor(
    private val songDao: SongDao
) {
    suspend fun insertSong(song: Song) = songDao.insertSong(song)

    suspend fun getSongById(songId: Long) : Song? = songDao.getSongById(songId)

    fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()
}