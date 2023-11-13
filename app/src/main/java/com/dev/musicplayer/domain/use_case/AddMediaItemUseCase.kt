package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.service.PlaybackController
import javax.inject.Inject

class AddMediaItemsUseCase @Inject constructor(
    private val playbackController: PlaybackController
) {
    operator fun invoke(musics: List<Song>) {
        playbackController.addMediaItems(musics)
    }
}