package com.dev.musicplayer.data.local.repositories

import com.dev.musicplayer.data.local.entities.Playlist
import com.dev.musicplayer.data.local.store.PlaylistStore
import com.dev.musicplayer.domain.entities.PlaylistEntity
import com.dev.musicplayer.domain.repositories.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    override fun getPlaylistsOrderedByName() : Flow<List<Playlist>> = playListStore.getPlaylistsOrderedByName()

    override suspend fun update(playlist: Playlist) = withContext(Dispatchers.IO) {
        playListStore.update(playlist)
    }
    override fun getAllPlaylists(): Flow<List<Playlist>> = playListStore.getAllPlaylists()

    override fun getPlaylistsOrderedByCreatedAt() : Flow<List<Playlist>> = playListStore.getPlaylistsOrderedByCreatedAt()


    override suspend fun getPlaylistById(playlistId: Long) : Playlist {
        return playListStore.getPlaylistById(playlistId)
    }
}


//fun Playlist.toEntity(): PlaylistEntity {
//    return PlaylistEntity (
//        id = id,
//        title = title,
//        image = thumbnail,
//        songs = songs,
//        createdAt = createdAt,
//    )
//}
