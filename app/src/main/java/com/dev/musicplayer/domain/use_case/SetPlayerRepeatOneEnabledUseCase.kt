package com.dev.musicplayer.domain.use_case

import com.dev.musicplayer.domain.service.PlaybackController
import javax.inject.Inject

class SetPlayerRepeatOneEnabledUseCase  @Inject constructor(
    private val playbackController: PlaybackController
) {
    operator fun invoke(isEnabled: Boolean) {
        playbackController.setRepeatOneEnabled(isEnabled)
    }
}