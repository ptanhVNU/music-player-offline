package com.dev.musicplayer.data.local.repositories

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.data.local.store.SongStore
import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.repositories.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepositoryImpl @Inject constructor(
    private val songStore: SongStore
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

    override fun getAllSongs() = songStore.getAllSongs()

    override suspend fun deleteSong(song: Song) = withContext(
        Dispatchers.IO
    ) {
        songStore.deleteSong(song)
    }

    override suspend fun editSong(song: Song) = withContext(Dispatchers.IO) {

        songStore.editSong(song)
    }


    override fun getSongsOrderedByName() = flow<List<MusicEntity>> {
        val songs = songStore.getSongsOrderedByName()

        songs.map { songList ->
            emit(songList.map {
                it.toEntity()
            })
        }
    }

    override fun getSongsOrderedByCreatedAt() = flow<List<MusicEntity>> {
        val songs = songStore.getSongsOrderedByCreatedAt()

        songs.map { songList ->
            emit(songList.map {
                it.toEntity()
            })
        }
    }

    override fun getLikedSongs() = flow<List<MusicEntity>> {

        val songs = songStore.getLikedSongs()

        songs.map { songList ->
            emit(songList.map {
                it.toEntity()
            })
        }
    }

}

fun Song.toEntity(): MusicEntity {
    return MusicEntity(
        id = songId.toString(),
        title = title,
        artist = artistName,
        source = uri,
        image = thumbnail,
        createdAt = createdAt,
    )
}