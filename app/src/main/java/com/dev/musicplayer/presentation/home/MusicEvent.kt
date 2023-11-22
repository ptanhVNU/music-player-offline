package com.dev.musicplayer.presentation.home

import com.dev.musicplayer.core.shared.models.MediaAudioItem

sealed class MusicEvent {
    data object PlayMusic : MusicEvent()
    data object ResumeMusic : MusicEvent()
    data object PauseMusic : MusicEvent()
    data class OnMusicSelected(val selectedMusic: MediaAudioItem) : MusicEvent()
}
