package com.dev.musicplayer.data.local.repositories

import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.store.PlaylistStore
import com.dev.musicplayer.domain.entities.PlaylistEntity
import com.dev.musicplayer.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val playListStore: PlaylistStore
) : PlaylistRepository {
    override suspend fun createPlaylist(title: String) = withContext(Dispatchers.IO) {
        val playlist = Playlist(
            title = title,
            createdAt = Instant.now().toEpochMilli(),
        )
        playListStore.createPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) = withContext(Dispatchers.IO) {
        playListStore.deletePlaylist(playlist.id)
    }

    override fun getPlaylistsOrderedByName() = flow<List<PlaylistEntity>> {
        val playlists = playListStore.getPlaylistsOrderedByName()

        playlists.map { list ->
            emit(list.map {
                it.toEntity()
            })
        }
    }

    override fun getAllPlaylists() = playListStore.getAllPlaylists()

    override fun getPlaylistsOrderedByCreatedAt() = flow<List<PlaylistEntity>> {
        val playlists = playListStore.getPlaylistsOrderedByCreatedAt()

        playlists.map { list ->
            emit(list.map {
                it.toEntity()
            })
        }
    }

    override suspend fun getPlaylistById(playlistId: Long) : Playlist {
        return playListStore.getPlaylistById(playlistId)
    }
}

fun Playlist.toEntity(): PlaylistEntity {
    return PlaylistEntity (
        id = id.toString(),
        title = title,
        image = thumbnail,
        songs = songs,
        createdAt = createdAt,
    )
}
