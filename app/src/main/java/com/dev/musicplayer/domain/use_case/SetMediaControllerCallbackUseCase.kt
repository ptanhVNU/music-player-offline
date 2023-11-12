package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.data.local.entities.Song
import com.dev.musicplayer.domain.service.PlaybackController
import com.dev.musicplayer.utils.PlayerState
import javax.inject.Inject

class SetMediaControllerCallbackUseCase  @Inject constructor(
    private val playbackController: PlaybackController
) {
    operator fun invoke(
        callback: (
            playerState: PlayerState,
            currentMusic: Song?,
            currentPosition: Long,
            totalDuration: Long,
            isShuffleEnabled: Boolean,
            isRepeatOneEnabled: Boolean
        ) -> Unit
    ) {
        playbackController.mediaControllerCallback = callback
    }
}