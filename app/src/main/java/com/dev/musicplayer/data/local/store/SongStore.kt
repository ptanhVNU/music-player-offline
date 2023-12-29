package com.dev.musicplayer.data.local.store

import com.dev.musicplayer.data.local.dao.SongDao
import com.dev.musicplayer.data.local.entities.Song
import javax.inject.Inject

class SongStore @Inject constructor(
    private val songDao: SongDao
) {
    suspend fun insertSong(song: Song) = songDao.insertSong(song)

    suspend fun getSongById(songId: Long) : Song? = songDao.getSongById(songId)
}