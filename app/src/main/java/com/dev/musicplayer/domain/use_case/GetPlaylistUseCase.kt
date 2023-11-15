package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.domain.repositories.PlaylistRepository
import javax.inject.Inject

class GetPlaylistUseCase @Inject constructor(private val playlistRepository: PlaylistRepository) {
    suspend operator fun invoke() = playlistRepository.getAllPlaylists()
}
