package com.dev.musicplayer.presentation.home

import com.dev.musicplayer.data.local.entities.Song

sealed class MusicEvent {
    data object PlayMusic : MusicEvent()
    data object ResumeMusic : MusicEvent()
    data object PauseMusic : MusicEvent()
    data class OnMusicSelected(val selectedMusic: Song) : MusicEvent()
}
