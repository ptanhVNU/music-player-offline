package com.dev.musicplayer.presentation.home

import com.dev.musicplayer.data.local.entities.Song

sealed class HomeEvent {
    object PlayMusic : HomeEvent()
    object ResumeMusic : HomeEvent()
    object PauseMusic : HomeEvent()
    data class OnMusicSelected(val selectedMusic: Song) : HomeEvent()
}
