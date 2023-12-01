package com.dev.musicplayer.data.local.components

import com.dev.musicplayer.core.ext.toMusicEntity
import com.dev.musicplayer.domain.entities.MusicEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuerySearch(
    private val daoCollection: DaoCollection,
) {
    /// convert song to music entity
    suspend fun searchSongs(query: String): List<MusicEntity> = withContext(Dispatchers.IO) {
        val songs = daoCollection.songDao.searchSongs(query)

        songs.map { song ->
            song.toMusicEntity()
        }
    }

    suspend fun searchPlaylists(query: String) = daoCollection.playlistDao.searchPlaylists(query)

}