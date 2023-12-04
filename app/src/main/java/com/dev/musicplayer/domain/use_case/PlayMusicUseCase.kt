package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.domain.service.PlaybackController
import javax.inject.Inject

class PlayMusicUseCase @Inject constructor(
    private val playbackController: PlaybackController
) {
    operator fun invoke(mediaItemsIndex: Int) {
        playbackController.play(mediaItemsIndex)
    }
}