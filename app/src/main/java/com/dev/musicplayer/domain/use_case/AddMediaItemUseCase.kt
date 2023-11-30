package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.domain.entities.MusicEntity
import com.dev.musicplayer.domain.service.PlaybackController

class AddMediaItemsUseCase(
    private val playbackController: PlaybackController
) {
    operator fun invoke(musics: List<MusicEntity>) {
        playbackController.addMediaItems(musics)
    }
}